package Factory;

import guet.depart3.yongyu.Builder.Builder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Properties;

class BuilderFactoryTest {

    @BeforeEach
    void setUp() {
        System.out.println("-----------测试开始-----------");
    }

    @AfterEach
    void tearDown() {
        System.out.println("------------测试结束-------------");
    }

    @Test
    void getProjectBuilder() {
        Builder builder = null;
        Properties pps =  new Properties();   //通过读取配置文件寻找对应的builder
        try{

            InputStream in = new BufferedInputStream(new FileInputStream("app/src/resources/config.properties"));
            pps.load(in);    //装载函数
            String value = pps.getProperty("java");
            builder = (Builder)Class.forName(value).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}