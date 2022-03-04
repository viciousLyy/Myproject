package Factory;

import Utils.Project;

public class ProjectFactory {
    private static ProjectFactory factory;
    private Project project;

    private ProjectFactory(){}

    public static ProjectFactory getInstance(){
        if(factory == null){
            factory = new ProjectFactory();
        }
        return factory;
    }

    public Project getProject(String path,String srcExt){

        return project;
    }
}
