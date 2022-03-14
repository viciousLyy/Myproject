package guet.depart3.yongyu.Builder;

import Utils.Compiler;
import Utils.Interpreter;
import Utils.Project;
import Utils.TextFile;
import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;

import java.util.List;

public  class MixedBuilder implements Builder{

    private final Compiler compiler;
    private final Interpreter interpreter;

    public MixedBuilder(Compiler c, Interpreter interpreter){
        this.compiler=c;
        this.interpreter=interpreter;
    }

    @Override
    public void run(Project project) throws CompilerException {
        List<String> res=compiler.compile(project);
        interpreter.interprete(project);
    }

    @Override
    public TextFile run(Project project, TextFile srcFile) {
        return null;
    }
}
