package Sub;

import Utils.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectSub extends Project {
    static String path;
    static String srcExt;

    public static ArrayList<File> allFiles = new ArrayList<File>();

    public ProjectSub(){}

    public ProjectSub(String path,String srcExt){
        this.path = path;
        this.srcExt = srcExt;
    }


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
}
