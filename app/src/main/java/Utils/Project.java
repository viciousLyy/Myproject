package Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Project {
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
        return null;
    }

    /*

     */
    public void setMainFileIndex(int index){

    }
}
