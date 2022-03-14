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
 * �����������࣬������һ���������࣬��������µı�����ԣ�����һ�������������࣬Ӧ�̳�CompilerAdapter
 * �࣬�����CompileAnnotationע����ʵ�֡����壬���Բο�JavaCompiler�Ķ���
 */
public abstract class Compiler {
    /**
     * �������������̵Ĺ���������
     */
    private final ProcessBuilder processBuilder;
    /**
     * �������������ɵ�Ŀ���ļ�����չ����Сд�������� exe
     */
    private String targetFileExt="";
    /**
     * �������ܹ������Դ�ļ���չ���б��� c.cpp
     */
    private final List<String> srcFileExts;
    /**
     * ����������ʾ����
     */
    private String compilerName="compiler";
    /**
     * ����������
     */
    private final List<String> cmdLine;
    /**
     * ���볬ʱʱ�䣬��λ����
     */
    private long timeout=30;


    /**
     * ���ر�������֧�ֵ�Դ�ļ��б�
     * @return ��������֧�ֵ�Դ�ļ��б�
     */
    public List<String> getSrcFileExts() {
        return srcFileExts;
    }


    /**
     * ����һ��û�г�ʼ�������в����ı���������
     */
    public Compiler() {
        processBuilder = new ProcessBuilder();
        srcFileExts = new ArrayList<>();
        cmdLine = new ArrayList<>();
    }

    /**
     * ����һ����ʼ���ı�����
     * @param cmdLine ��ռλ���������ж���ռλ���Ķ������CompileCommandע�⡣
     * @param srcExts ���������ܱ����Դ�ļ�����չ�����ϣ���{"c","cpp"},Сд
     * @param targetFileExt �������ɵ��ļ�����չ������exe
     */
    public Compiler(String[]cmdLine,String[]srcExts,String targetFileExt){
        processBuilder=new ProcessBuilder();
        srcFileExts= ListUtil.array2List(srcExts);
        this.cmdLine=ListUtil.array2List(cmdLine);
        this.setTargetFileExt(targetFileExt);
    }
    /**
     * ���ñ������ܹ������Դ�ļ���չ���ķ�Χ
     *
     * @param exts Դ�ļ���չ�� ����չ���б�Ԫ�ر����Ƶ���������srcFileExts��
     */
    public void setSrcFileExt(List<String> exts) {
        srcFileExts.clear();
        srcFileExts.addAll(exts);
    }

    /**
     * ���ñ�����������ɵ�Ŀ���ļ�����չ��
     *
     * @param ext Ŀ���ļ�����չ��
     */
    public void setTargetFileExt(String ext) {
        this.targetFileExt = ext;
    }


    /**
     * ��һ����Ŀ���б��빹��,�ù������������ĿԴ�ļ���չ������������ܱ����Դ�ļ�����չ���Ƿ�ƥ��
     *
     * @param project ����������Ŀ
     * @return ���������ɵ��ļ��б�
     * @throws CompilerException �����﷨����ʱ�������������������ʱ
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
            throw new CompilerException("�Ҳ�������������");
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
     * ���ݵ�ǰProject������������������cmd�е�Ĭ��ռλ����Ĭ��ռλ������CompileCommand�ж���ļ���������
     * @param cmd �����ж���
     * @param project ��ǰҪ�������Ŀ
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
     * ����������еĳ�CompileCommand���ռλ��(��{}��ʾ���ַ�����
     * @param extraPlaceHolder ����ռλ���б�
     * @param cmd �����ж���
     * @param project ��Ŀ
     */
    protected abstract void populateExtraPlaceHolder(List<String> extraPlaceHolder, List<String> cmd, Project project);

    /**
     * ��ȡ�������г�CompileCommand����֮���ռλ��
     * @param cmd ������
     * @return �����ռλ��
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
     * ���ý��̹�����������Ϊԭʼ�Ĵ�ռλ���������ж���
     * @return
     */
    private List<String> resetCmdLine() {
        List<String> cmd = processBuilder.command();
        cmd.clear();
        cmd.addAll(cmdLine);
        return cmd;
    }

    /**
     * ���Ŀ��Ŀ¼�����б������ɵ�Ŀ���ļ���·��
     *
     * @param targetPath Ŀ��Ŀ¼
     * @return �������ɵ�Ŀ���ļ��б�
     */
    private List<String> getCompileResultFilePaths(File targetPath) {
        List<File> result = new ArrayList<>();
        FileUtil.findFiles(result,targetPath, Collections.singletonList(targetFileExt));
        return FileUtil.getPathsOfFiles(result);
    }

    /**
     * ��ָ����Ŀ¼�´��������ļ����󣨸��ļ���û�б�ʵ�ʴ�����
     *
     * @param targetPath Ŀ��Ŀ¼
     * @return �����ļ������ļ���Ϊerror.txt)
     */
    public static TextFile createErrTextFile(File targetPath) {
        String errFilePath = targetPath.getAbsolutePath() + File.separator
                + "error.txt";
        return new TextFile(errFilePath);
    }

    /**
     * ���ñ��������������С��ò��������������cmdLine,Ȼ���cmd��ֵ���Ƶ�cmdLine�ϡ�
     *
     * @param cmd ���������У�������{srcFiles},{targetPath},{targetFile}��ռλ��Ԫ��
     */
    public void setCommand(List<String> cmd) {
        cmdLine.clear();
        cmdLine.addAll(cmd);
    }

    /**
     * ���ر���������ʾ����
     * @return ��������ʾ����
     */
    public String getCompilerName() {
        return compilerName;
    }

    /**
     * ���ñ���������ʾ����
     * @param compilerName ��������ʾ����
     */
    public void setCompilerName(String compilerName) {
        this.compilerName = compilerName;
    }

    /**
     * ���ر������ɵ�Ŀ���ļ�����չ��
     * @return Ŀ���ļ���չ������class
     */
    public String getTargetFileExt() {
        return targetFileExt;
    }

    /**
     * ���ر��������볬ʱʱ�䣬��λ����
     * @return ��ʱʱ�䣬��λ����
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * ���ñ�������ʱʱ�䣬��λ����
     * @param timeout ��ʱʱ�䣬��
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * ���������ж���,���Ǵ�ռλ���������ж���
     * @return �����ж���
     */
    public List<String> getCommand() {
        return cmdLine;
    }

}
