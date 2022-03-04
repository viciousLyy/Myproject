package Builder;

import Utils.Project;
import Utils.TextFile;

import java.io.File;

public abstract class CompileBuilder implements Builder{


    public File compile(Project project){
        return null;
    }

    public File link(Project project){
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
