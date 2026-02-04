package com.bage.study.best.practice.trial.gc;

/**
 * JVM垃圾回收器配置说明
 */
public class GCConfig {

    /**
     * Serial收集器配置
     */
    public static final String SERIAL_GC_CONFIG = "-XX:+UseSerialGC " +
            "-Xms512m -Xmx512m " +
            "-Xmn256m " +
            "-XX:SurvivorRatio=8 " +
            "-XX:+PrintGCDetails " +
            "-XX:+PrintGCDateStamps " +
            "-Xloggc:serial-gc.log";

    /**
     * Parallel收集器配置
     */
    public static final String PARALLEL_GC_CONFIG = "-XX:+UseParallelGC " +
            "-XX:+UseParallelOldGC " +
            "-Xms1g -Xmx1g " +
            "-Xmn512m " +
            "-XX:SurvivorRatio=8 " +
            "-XX:ParallelGCThreads=4 " +
            "-XX:MaxGCPauseMillis=100 " +
            "-XX:+PrintGCDetails " +
            "-XX:+PrintGCDateStamps " +
            "-Xloggc:parallel-gc.log";

    /**
     * CMS收集器配置
     */
    public static final String CMS_GC_CONFIG = "-XX:+UseConcMarkSweepGC " +
            "-XX:+UseParNewGC " +
            "-Xms1g -Xmx1g " +
            "-Xmn512m " +
            "-XX:SurvivorRatio=8 " +
            "-XX:CMSInitiatingOccupancyFraction=75 " +
            "-XX:+UseCMSInitiatingOccupancyOnly " +
            "-XX:+CMSParallelRemarkEnabled " +
            "-XX:+CMSScavengeBeforeRemark " +
            "-XX:+PrintGCDetails " +
            "-XX:+PrintGCDateStamps " +
            "-Xloggc:cms-gc.log";

    /**
     * G1收集器配置
     */
    public static final String G1_GC_CONFIG = "-XX:+UseG1GC " +
            "-Xms1g -Xmx1g " +
            "-XX:MaxGCPauseMillis=200 " +
            "-XX:ParallelGCThreads=4 " +
            "-XX:ConcGCThreads=2 " +
            "-XX:InitiatingHeapOccupancyPercent=45 " +
            "-XX:G1HeapRegionSize=16m " +
            "-XX:G1MaxNewSizePercent=60 " +
            "-XX:G1ReservePercent=15 " +
            "-XX:+PrintGCDetails " +
            "-XX:+PrintGCDateStamps " +
            "-XX:+PrintAdaptiveSizePolicy " +
            "-Xloggc:g1-gc.log";

    /**
     * ZGC收集器配置 (JDK 11+)
     */
    public static final String ZGC_GC_CONFIG = "-XX:+UseZGC " +
            "-Xms1g -Xmx1g " +
            "-XX:ZGCHeapSizeMax=1g " +
            "-XX:ZGCHeapSizeMin=512m " +
            "-XX:+PrintGCDetails " +
            "-XX:+PrintGCDateStamps " +
            "-Xloggc:zgc-gc.log";

    /**
     * 内存分配策略测试配置
     */
    public static final String MEMORY_ALLOCATION_CONFIG = "-XX:+UseG1GC " +
            "-Xms256m -Xmx256m " +
            "-XX:MaxGCPauseMillis=100 " +
            "-XX:+PrintGCDetails " +
            "-XX:+PrintGCDateStamps " +
            "-Xloggc:memory-allocation.log";

}
