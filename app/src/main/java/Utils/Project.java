package Utils;

import java.io.File;
import java.util.List;

public class Project {
    static String path;
    static String srcExt;

    public Project(){

    }

    public Project(String path,String srcExt){
        this.path = path;
        this.srcExt = srcExt;
    }

    /*
    获得所有的文件
     */
    public List<File> getMainFiles(){
        return null;
    }

    /*

     */
    public void setMainFileIndex(int index){

    }
}
