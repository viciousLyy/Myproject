package Utils;

import Builder.CompileBuilder;
import Builder.MixedBuilder;

import java.io.File;
import java.util.List;

public abstract class Compiler extends CompileBuilder {
    private List<String> cmdLine;
    private String workDir;
    private Project project;

    public String getCharset(){
        return null;
    }

    @Override
    public File compile(Project project){
        return null;
    }

    @Override
    public void run(Project project) {

    }

    @Override
    public TextFile run(Project project, TextFile srcFile) {
        return null;
    }


}
