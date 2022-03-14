package Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    /**
     * 从配置文件中根据指定的参数得到对应的值
     * @param key 指定的参数
     * @return 参数对应的值
     */
    public static String getParameter(String key){
        String value = null;
        Properties properties = new Properties();
        FileInputStream in = null;
        try{
            in = new FileInputStream("app/resources/config.properties");
            properties.load(in);
            value = properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(in!=null){
                try{
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
