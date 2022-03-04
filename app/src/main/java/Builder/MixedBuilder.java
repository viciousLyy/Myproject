package Builder;

import Utils.Project;
import Utils.TextFile;

public abstract class MixedBuilder implements Builder{

    public void  compile(){

    }

    @Override
    public void run(Project project) {

    }

    @Override
    public TextFile run(Project project, TextFile srcFile) {
        return null;
    }
}
