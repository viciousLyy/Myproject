package Utils;

import java.util.ArrayList;
import java.util.List;

public class GccCompiler extends Compiler{

    public GccCompiler(){
        List<String>exts=new ArrayList<>();
        exts.add("c");
        exts.add("cpp");
        this.setSrcFileExt(exts);
        this.setTargetFileExt("exe");
        this.setCompilerName("GCC编译器");
        List<String> cmd=new ArrayList<>();
        cmd.add("gcc");
        cmd.add(CompileCommand.sourceFiles);
        this.setCommand(cmd);

    }
    @Override
    protected void populateExtraPlaceHolder(List<String> extraPlaceHolder, List<String> cmd, Project project) {

    }
}
