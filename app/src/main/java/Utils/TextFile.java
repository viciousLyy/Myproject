package Utils;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class TextFile {
    private String path;

    public TextFile(){}

    public TextFile(String path){
        this.path = path;
    }

    /*
    读取文件的所有内容
     */
    public List<String> getContents(){
        File file = new File(path);
        String charSet = getCharSet();
        try(
                InputStream ins =new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(ins,charSet);
                BufferedReader br = new BufferedReader(isr);
                ) {
            List<String> contents = new ArrayList<>();
            String s = br.readLine();
            while(s!=null){
                contents.add(s);
                s = br.readLine();
            }
            return contents;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /*
    读取文件的类型码
     */
    public String getCharSet(){
        File file = new File(path);
        byte[] head = new byte[3];
        try(
                FileInputStream stream = new FileInputStream(file);
                ) {
            stream.read(head);
            String code = "gb2312";
            if (head[0] == -1 && head[1] == -2 )
                code = "UTF-16";
            else if (head[0] == -2 && head[1] == -1 )
                code = "Unicode";
            else if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
                code = "UTF-8";
            return code;
        } catch (FileNotFoundException e) {
            return "gb2312";
        } catch (IOException e) {
            return "gb2312";
        }
    }

}
