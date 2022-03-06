package Factory;

import Builder.Builder;

/*
单例类
 */
public class BuilderFactory {
    private static BuilderFactory factory;
    private Builder builder;

    private BuilderFactory(){}            //对构造器使用private修饰，隐藏该构造器

    public static BuilderFactory getInstance(){
        /*
        如果factory为空，则表明还不曾创建factory对象，将会创建新的实例
         */
        synchronized (BuilderFactory.class){    //加锁解决多线程访问不同步问题
            if(factory == null){
                factory = new BuilderFactory();
            }
        }
        return factory;
    }

    public Builder getProjectBuilder(String srcExt){

        return builder;
    }
}
