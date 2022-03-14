package Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Project {
    static String path;
    static String srcExt;

    public static ArrayList<File> allFiles = new ArrayList<File>();

    public Project(){}

    public Project(String path,String srcExt){
        this.path = path;
        this.srcExt = srcExt;
    }

    /*
    获得所有的文件
     */
    public List<File> getMainFiles(String path){
        File[] files = new File(path).listFiles();
        for(File file:files){
            if(file.isDirectory())
                getMainFiles(file.getAbsolutePath());
            else
                allFiles.add(file);
        }
        return allFiles;
    }

    /*

     */
    public void setMainFileIndex(int index){

    }

    public File getOutputDir(){
        return null;
    }

    public List<File> getSrcFiles(){
        return null;
    }

    public String getProjectName() {
        File file = new File(path);
        String rolePath = "";
        String projectName = "";
        try {
            rolePath = file.getCanonicalPath();
            projectName = rolePath.substring(rolePath.lastIndexOf("\\") + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectName;
    }
}
