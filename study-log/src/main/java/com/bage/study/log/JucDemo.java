package com.bage.study.log;

import java.io.IOException;
import java.util.logging.*;

public class JucDemo {
    public static void main(String[] args) throws IOException {
        demo1();

        demo2();
    }

    private static void demo2() throws IOException {
        // 1.获取日志对象
        Logger logger = Logger.getLogger("com.zyb.LoggerTest");
        config2(logger);

        // 2.日志记录输出
        logger.severe("severe");
        logger.warning("warning");
        logger.info("info");
        logger.config("cofnig");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");
    }

    private static void demo1() {
        //获取日志记录器
        //一般每一个日志记录器都有一个唯一的标识，我们一般拿当前类的全限定名来充当这个角色
        Logger logger = Logger.getLogger("JucDemo");
        config1(logger);


        logger.info("hello");

        logger.log(Level.INFO, "this is info");

        //如果只有一个{}可以不用标序号，多个一定要标
        logger.log(Level.WARNING, "{0}发生了错误{1}", new Object[]{"JucDemo", "!"});

    }

    private static void config2(Logger logger) throws IOException {
        logger.setLevel(Level.WARNING);

        //再绑定一个文件Handler
        FileHandler fileHandler = new FileHandler("./study-log/logger-info.txt");
        Formatter format = new SimpleFormatter();
        fileHandler.setFormatter(format);
        //进行绑定
        logger.addHandler(fileHandler);
    }
    private static void config1(Logger logger) {
        //设置日志级别
        logger.setLevel(Level.INFO);
        //设置控制台的Handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        //设置Handler的日志级别
        consoleHandler.setLevel(Level.ALL);
        //绑定Handler
        logger.addHandler(consoleHandler);
    }
}
