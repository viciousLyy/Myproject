package Builder;

import Utils.Project;
import Utils.TextFile;

public abstract class InterpreteBuilder implements Builder{

    @Override
    public void run(Project project) {

    }

    @Override
    public TextFile run(Project project, TextFile srcFile) {
        return null;
    }
}
