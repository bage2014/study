package com.bage.study.springboot.util;

import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.boot.system.ApplicationTemp;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

/**
 * https://juejin.cn/post/7386497602194685993
 */
public class SpringUtils{
    public static void getPid(){
        ApplicationPid pid = new ApplicationPid() ;
        System.out.printf("current pid = " +  pid.toString() + "\n") ;
    }

    public static void getMainDir(){
        ApplicationHome home = new ApplicationHome() ;
        System.out.printf("dir: %s, source: %s%n", home.getDir(), home.getSource() + "\n") ;
    }
    public static void getJavaVersion(){
        System.out.printf("Java Version: %s%n", JavaVersion.getJavaVersion() + "\n") ;
    }
    public static void getTempDir(){
        ApplicationTemp temp = new ApplicationTemp() ;
        System.out.printf("temp dir: %s%n", temp.getDir() + "\n") ;
    }

    public static void getYmlFile() throws IOException {
        //    / 加载yaml文件
//        YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader() ;
        PropertiesPropertySourceLoader propertyLoader = new PropertiesPropertySourceLoader() ;
        List<PropertySource<?>> yamls = propertyLoader.load("my.app.value", new ClassPathResource("application.properties")) ;
        System.out.printf("pack.*: %s%n", yamls.get(0).getSource() + "\n") ;
    }


    public static void getBasePackages(ConfigurableApplicationContext context) {
        System.out.printf("getBasePackages: %s%n", AutoConfigurationPackages.get(context) + "\n") ;
    }



}
