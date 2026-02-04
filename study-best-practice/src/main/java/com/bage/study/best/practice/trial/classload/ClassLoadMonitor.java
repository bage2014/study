package com.bage.study.best.practice.trial.classload;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类加载监控工具
 */
@Slf4j
public class ClassLoadMonitor {

    // 类加载信息映射
    private static final Map<String, ClassLoadInfo> classLoadInfoMap = new HashMap<>();

    // 实例化计数器
    private static final Map<String, AtomicInteger> instanceCountMap = new HashMap<>();

    /**
     * 初始化监控器
     */
    public static void init(Instrumentation instrumentation) {
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                String canonicalName = className.replace('/', '.');
                
                // 记录类加载信息
                ClassLoadInfo info = new ClassLoadInfo();
                info.setClassName(canonicalName);
                info.setLoaderName(loader != null ? loader.getClass().getName() : "Bootstrap ClassLoader");
                info.setLoadTime(System.currentTimeMillis());
                info.setLoaded(true);
                
                classLoadInfoMap.put(canonicalName, info);
                instanceCountMap.putIfAbsent(canonicalName, new AtomicInteger(0));
                
                log.info("Class loaded: {} by {}", canonicalName, info.getLoaderName());
                
                return classfileBuffer;
            }
        });
    }

    /**
     * 记录类初始化
     */
    public static void recordClassInit(String className) {
        ClassLoadInfo info = classLoadInfoMap.get(className);
        if (info != null) {
            info.setInitialized(true);
            info.setInitTime(System.currentTimeMillis());
            log.info("Class initialized: {}", className);
        }
    }

    /**
     * 记录实例创建
     */
    public static void recordInstanceCreation(String className) {
        AtomicInteger counter = instanceCountMap.get(className);
        if (counter != null) {
            int count = counter.incrementAndGet();
            log.info("Instance created: {}, count = {}", className, count);
        }
    }

    /**
     * 获取类加载信息
     */
    public static ClassLoadInfo getClassLoadInfo(String className) {
        return classLoadInfoMap.get(className);
    }

    /**
     * 获取实例化计数
     */
    public static int getInstanceCount(String className) {
        AtomicInteger counter = instanceCountMap.get(className);
        return counter != null ? counter.get() : 0;
    }

    /**
     * 类加载信息
     */
    public static class ClassLoadInfo {
        private String className;
        private String loaderName;
        private long loadTime;
        private long initTime;
        private boolean loaded;
        private boolean initialized;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getLoaderName() {
            return loaderName;
        }

        public void setLoaderName(String loaderName) {
            this.loaderName = loaderName;
        }

        public long getLoadTime() {
            return loadTime;
        }

        public void setLoadTime(long loadTime) {
            this.loadTime = loadTime;
        }

        public long getInitTime() {
            return initTime;
        }

        public void setInitTime(long initTime) {
            this.initTime = initTime;
        }

        public boolean isLoaded() {
            return loaded;
        }

        public void setLoaded(boolean loaded) {
            this.loaded = loaded;
        }

        public boolean isInitialized() {
            return initialized;
        }

        public void setInitialized(boolean initialized) {
            this.initialized = initialized;
        }

        @Override
        public String toString() {
            return "ClassLoadInfo{
" +
                    "  className='" + className + "'\n" +
                    "  loaderName='" + loaderName + "'\n" +
                    "  loadTime='" + new java.util.Date(loadTime) + "'\n" +
                    "  initTime='" + (initTime > 0 ? new java.util.Date(initTime) : "Not initialized") + "'\n" +
                    "  loaded=" + loaded + "\n" +
                    "  initialized=" + initialized + "\n" +
                    '}';
        }
    }

}
