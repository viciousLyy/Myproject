package Utils;

import Builder.InterpreteBuilder;

import java.util.List;

public abstract class Interpreter extends InterpreteBuilder {
    private String worDir;
    private List<String> cmdLine;

    @Override
    public void run(Project project) {

    }

    @Override
    public TextFile run(Project project, TextFile srcFile) {
        return null;
    }
}
