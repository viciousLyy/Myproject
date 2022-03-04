package Builder;


import Utils.Project;
import Utils.TextFile;

public interface Builder {

    public void run(Project project);

    public TextFile run(Project project,TextFile srcFile);

}
