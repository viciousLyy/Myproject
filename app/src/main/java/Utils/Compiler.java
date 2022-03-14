package Utils;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.tools.JavaCompiler;

/**
 * 表抽象编译器类，该类有一个适配器类，若想针对新的编程语言，定义一个编译器具体类，应继承CompilerAdapter
 * 类，并结合CompileAnnotation注解来实现。具体，可以参考JavaCompiler的定义
 */
public abstract class Compiler {
    /**
     * 创建编译器进程的构建器对象
     */
    private final ProcessBuilder processBuilder;
    /**
     * 编译器编译生成的目标文件的扩展名（小写），例如 exe
     */
    private String targetFileExt="";
    /**
     * 编译器能够编译的源文件扩展名列表，如 c.cpp
     */
    private final List<String> srcFileExts;
    /**
     * 编译器的显示名称
     */
    private String compilerName="compiler";
    /**
     * 编译命令行
     */
    private final List<String> cmdLine;
    /**
     * 编译超时时间，单位：秒
     */
    private long timeout=30;


    /**
     * 返回编译器所支持的源文件列表
     * @return 编译器所支持的源文件列表
     */
    public List<String> getSrcFileExts() {
        return srcFileExts;
    }


    /**
     * 创建一个没有初始化命令行参数的编译器对象
     */
    public Compiler() {
        processBuilder = new ProcessBuilder();
        srcFileExts = new ArrayList<>();
        cmdLine = new ArrayList<>();
    }

    /**
     * 创建一个初始化的编译器
     * @param cmdLine 带占位符的命令行对象，占位符的定义详见CompileCommand注解。
     * @param srcExts 编译器所能编译的源文件的扩展名集合，如{"c","cpp"},小写
     * @param targetFileExt 编译生成的文件的扩展名，如exe
     */
    public Compiler(String[]cmdLine,String[]srcExts,String targetFileExt){
        processBuilder=new ProcessBuilder();
        srcFileExts= ListUtil.array2List(srcExts);
        this.cmdLine=ListUtil.array2List(cmdLine);
        this.setTargetFileExt(targetFileExt);
    }
    /**
     * 设置编译器能够编译的源文件扩展名的范围
     *
     * @param exts 源文件扩展名 该扩展名列表元素被复制到编译器的srcFileExts中
     */
    public void setSrcFileExt(List<String> exts) {
        srcFileExts.clear();
        srcFileExts.addAll(exts);
    }

    /**
     * 设置编译器编程生成的目标文件的扩展名
     *
     * @param ext 目标文件的扩展名
     */
    public void setTargetFileExt(String ext) {
        this.targetFileExt = ext;
    }


    /**
     * 对一个项目进行编译构建,该构建并不检测项目源文件扩展名与编译器所能编译的源文件的扩展名是否匹配
     *
     * @param project 待构建的项目
     * @return 构建后生成的文件列表
     * @throws CompilerException 存在语法错误时或编译器不能正常工作时
     */
    public final List<String> compile(Project project) throws CompilerException {
        File outputDir=project.getOutputDir();
        processBuilder.directory(outputDir);
        TextFile errFile = createErrTextFile(outputDir);
        processBuilder.redirectError(new File(outputDir.getAbsolutePath() + File.separator
                + "error.txt"));
        List<String> cmd = resetCmdLine();
        populatePlaceHolder(cmd,project);
        Process p = null;
        try {
            p = this.processBuilder.start();
        } catch (IOException e) {
            throw new CompilerException("找不到编译器程序");
        }
        try {
            p.waitFor(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new CompilerException(e.getMessage());
        }
        List<String> errLine = errFile.getContents();
        if (errLine != null) {
            StringBuilder sb = new StringBuilder();
            errLine.forEach(line -> sb.append(line).append("\n"));
            throw new CompilerException(sb.toString());
        }
        return getCompileResultFilePaths(outputDir);

    }

    /**
     * 根据当前Project对象，填充编译器命令行cmd中的默认占位符（默认占位符就是CompileCommand中定义的几个常量）
     * @param cmd 命令行对象
     * @param project 当前要编译的项目
     */
    protected void populatePlaceHolder(List<String> cmd, Project project){
        ListUtil.replaceFirst(cmd,CompileCommand.sourceFiles,
                FileUtil.getPathsOfFiles(project.getSrcFiles()));
        ListUtil.replaceFirst(cmd,CompileCommand.targetPath,
                project.getOutputDir().getAbsolutePath());
        String targetFilePath=project.getOutputDir().getAbsolutePath()
                +File.separator+project.getProjectName()
                +"."+targetFileExt;
        ListUtil.replaceFirst(cmd,CompileCommand.targetFile,
                targetFilePath);
        List<String> extraPlaceHolder=getExtraPlaceHolder(cmd);
        if(extraPlaceHolder.size()>0){
            populateExtraPlaceHolder(extraPlaceHolder,cmd,project);
        }
    }



    /**
     * 填充命令行中的除CompileCommand外的占位符(以{}表示的字符串）
     * @param extraPlaceHolder 额外占位符列表
     * @param cmd 命令行对象
     * @param project 项目
     */
    protected abstract void populateExtraPlaceHolder(List<String> extraPlaceHolder, List<String> cmd, Project project);

    /**
     * 获取命令行中除CompileCommand定义之外的占位符
     * @param cmd 命令行
     * @return 额外的占位符
     */
    public static List<String> getExtraPlaceHolder(List<String> cmd) {
        List<String>result=new ArrayList<>();
        String reg="\\{.+}";
        for(String s:cmd){
            if(s.matches(reg)){
                result.add(s);
            }
        }
        return result;
    }

    /**
     * 重置进程构建器的命令为原始的带占位符的命令行对象
     * @return
     */
    private List<String> resetCmdLine() {
        List<String> cmd = processBuilder.command();
        cmd.clear();
        cmd.addAll(cmdLine);
        return cmd;
    }

    /**
     * 获得目标目录下所有编译生成的目标文件的路径
     *
     * @param targetPath 目标目录
     * @return 编译生成的目标文件列表
     */
    private List<String> getCompileResultFilePaths(File targetPath) {
        List<File> result = new ArrayList<>();
        FileUtil.findFiles(result,targetPath, Collections.singletonList(targetFileExt));
        return FileUtil.getPathsOfFiles(result);
    }

    /**
     * 在指定的目录下创建错误文件对象（该文件并没有被实际创建）
     *
     * @param targetPath 目标目录
     * @return 错误文件（其文件名为error.txt)
     */
    public static TextFile createErrTextFile(File targetPath) {
        String errFilePath = targetPath.getAbsolutePath() + File.separator
                + "error.txt";
        return new TextFile(errFilePath);
    }

    /**
     * 设置编译器编译命令行。该操作首先清空属性cmdLine,然后把cmd的值复制到cmdLine上。
     *
     * @param cmd 编译命令行，它包含{srcFiles},{targetPath},{targetFile}等占位符元素
     */
    public void setCommand(List<String> cmd) {
        cmdLine.clear();
        cmdLine.addAll(cmd);
    }

    /**
     * 返回编译器的显示名称
     * @return 编译器显示名称
     */
    public String getCompilerName() {
        return compilerName;
    }

    /**
     * 设置编译器的显示名称
     * @param compilerName 编译器显示名称
     */
    public void setCompilerName(String compilerName) {
        this.compilerName = compilerName;
    }

    /**
     * 返回编译生成的目标文件的扩展名
     * @return 目标文件扩展名，如class
     */
    public String getTargetFileExt() {
        return targetFileExt;
    }

    /**
     * 返回编译器编译超时时间，单位，秒
     * @return 超时时间，单位：秒
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * 设置编译器超时时间，单位，秒
     * @param timeout 超时时间，秒
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * 返回命令行对象,它是带占位符的命令行对象
     * @return 命令行对象
     */
    public List<String> getCommand() {
        return cmdLine;
    }

}
