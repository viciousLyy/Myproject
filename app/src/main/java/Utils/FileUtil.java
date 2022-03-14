package Utils;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private FileUtil(){}

//    public static List<String> getPathsOfFiles(List<File> srcFiles){
//        return null;
//    }


    //�ҵ�Ŀ��·���µ�������singleListΪ��׺���ļ�

    public static void findFiles(List<File> result, File targetPath, List<String> singletonList) {
        if(targetPath.isDirectory()){
            File[] fs = targetPath.listFiles();
            for(int i=0;i<fs.length;i++){
                findFiles(result,new File(fs[i].getPath()),singletonList);
            }
        }else if(targetPath.getName().endsWith("."+singletonList.get(0))){
            result.add(targetPath);
        }
    }

    public static List<String> getPathsOfFiles(List<File> srcFiles) {
        List<String> result = new ArrayList<String>();
        for(File f:srcFiles){
            result.add(f.getAbsolutePath());
        }
        return result;
    }
}
