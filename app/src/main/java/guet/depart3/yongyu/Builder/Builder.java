package guet.depart3.yongyu.Builder;


import Utils.Project;
import Utils.TextFile;
import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;

public interface Builder {

    public void run(Project project) throws CompilerException;

    public TextFile run(Project project,TextFile srcFile);

}
