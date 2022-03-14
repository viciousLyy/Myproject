package guet.depart3.yongyu.Builder.Factory;

import guet.depart3.yongyu.Builder.Builder;

import java.io.*;
import java.util.Properties;


public class BuilderFactory {
    private static BuilderFactory factory;


    private BuilderFactory(){}

    public static BuilderFactory getInstance(){

        synchronized (BuilderFactory.class){
            if(factory == null){
                factory = new BuilderFactory();
            }
        }
        return factory;
    }

    public Builder getProjectBuilder(String srcExt){
        Builder builder = null;
        Properties pps =  new Properties();
        try{
            InputStream in = new BufferedInputStream(new FileInputStream("app/resources/config.properties"));
            pps.load(in);
            String value = pps.getProperty(srcExt);
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
        return builder;
    }
}
