# study-best-practice



## 环境准备 

**基于Docker环境**

### Docker

https://github.com/bage2014/study/tree/master/study-docker

### MySQL

https://github.com/bage2014/study/tree/master/study-docker

启动

```
docker run --name bage-mysql -v ${HOME}/bage/docker-data/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=bage -p 3306:3306 -d mysql/mysql-server
```



### ES 

https://github.com/bage2014/study/tree/master/study-docker

### JVM

https://github.com/bage2014/study/tree/master/study-docker



## 机器信息 

```
	芯片：	Apple M1
  核总数：	8（4性能和4能效）
  内存：	16 GB
```



## 启动命令

```
启动

cd /Users/bage/bage/github/study/study-best-practice

java -jar -Xlog:gc:my-gc.log:time,level -Xms64m -Xmx256m target/study-best-practice-0.0.1-SNAPSHOT.jar

```



## CPU 查看

```
top

----0 QPS----

Processes: 474 total, 3 running, 471 sleeping, 2766 threads            08:51:10
Load Avg: 1.35, 1.67, 1.91  CPU usage: 15.23% user, 7.43% sys, 77.33% idle
SharedLibs: 440M resident, 89M data, 108M linkedit.
MemRegions: 251913 total, 5410M resident, 739M private, 3457M shared.
PhysMem: 15G used (1513M wired), 102M unused.
VM: 189T vsize, 3272M framework vsize, 0(0) swapins, 0(0) swapouts.
Networks: packets: 1603397/1093M in, 1837984/1074M out.
Disks: 557660/13G read, 113712/2110M written.

PID   COMMAND      %CPU  TIME     #TH    #WQ  #PORT MEM    PURG   CMPRS  PGRP
75    fseventsd    81.1  00:36.26 12/1   1    266   4898K  0B     704K   75
144   WindowServer 19.9  04:19.81 21     5    3985  609M-  15M+   64M    144
0     kernel_task  14.4  03:27.33 479/8  0    0     7840K+ 0B     0B     0
1806  Terminal     10.2  00:29.08 8      3    294-  113M+  6192K  11M    1806
2781  QQMusic      9.6   01:11.85 57     6    721+  149M+  96K+   33M-   2781
749   qemu-system- 6.5   06:41.35 9      0    24    8872M  0B     455M   570
2791  com.apple.We 6.2   00:35.60 4      2    103   105M   0B     19M    2791
111   opendirector 6.1   00:05.71 9      8    1205- 14M    0B     5216K  111
2859  top          4.7   00:31.99 1/1    0    47+   5137K  0B     880K   2859


----50 QPS----
Processes: 461 total, 3 running, 1 stuck, 457 sleeping, 2752 threads   08:43:19
Load Avg: 3.59, 2.41, 2.10  CPU usage: 16.62% user, 19.31% sys, 64.5% idle
SharedLibs: 420M resident, 85M data, 107M linkedit.
MemRegions: 250014 total, 5338M resident, 707M private, 3419M shared.
PhysMem: 15G used (1536M wired), 118M unused.
VM: 185T vsize, 3272M framework vsize, 0(0) swapins, 0(0) swapouts.
Networks: packets: 978227/512M in, 1078644/470M out.
Disks: 487012/12G read, 99596/1839M written.

PID   COMMAND      %CPU  TIME     #TH    #WQ  #PORT MEM    PURG   CMPRS  PGRP
749   qemu-system- 100.6 05:06.30 8/2    0    23    8869M  0B     454M   570
2692  java         50.3  03:08.87 51     1    171   325M+  0B     18M    2692
144   WindowServer 31.6  03:31.52 22     6    2340  540M-  21M+   55M    144
0     kernel_task  20.7  02:44.30 479/9  0    0     7776K+ 0B     0B     0
1806  Terminal     15.4  00:24.00 8      3    296   111M+  80K    6576K- 1806
2781  QQMusic      13.9  00:23.52 58     7    637+  149M+  976K+  32M    2781
2791  com.apple.We 4.6   00:09.93 5      3    104   98M    30M    15M    2791
1030  idea         4.3   07:18.72 77     5    556   2177M  128K   5389M- 1030
742   com.docker.v 3.8   00:11.90 5      0    18    28M    0B     3520K  570
570   com.docker.b 3.8   00:10.81 25     1    111   34M    0B     7376K  570
744   vpnkit-bridg 3.6   00:10.59 13     0    33    12M    0B     1568K  570
745   com.docker.d 3.5   00:12.15 16     1    53    16M+   0B     1632K  570
2097  java         2.4   00:28.66 48     4    371   1355M+ 0B     208M   2091


----200 QPS----
Processes: 472 total, 6 running, 466 sleeping, 2817 threads            08:53:45
Load Avg: 8.59, 4.26, 2.89  CPU usage: 34.66% user, 29.46% sys, 35.86% idle
SharedLibs: 440M resident, 89M data, 108M linkedit.
MemRegions: 251570 total, 5404M resident, 738M private, 3458M shared.
PhysMem: 15G used (1468M wired), 103M unused.
VM: 189T vsize, 3272M framework vsize, 0(0) swapins, 0(0) swapouts.
Networks: packets: 2603889/1382M in, 2873030/1378M out.
Disks: 588184/13G read, 118708/2220M written.

PID   COMMAND      %CPU  TIME     #TH    #WQ  #PORT MEM    PURG   CMPRS  PGRP
2692  java         172.1 06:25.41 63/3   1    195   437M+  0B     6240K- 2692
749   qemu-system- 163.3 09:43.93 9/3    0    24    8873M  0B     543M   570
144   WindowServer 31.9  04:54.09 22     6    3881- 609M-  15M+   80M    144
1806  Terminal     26.6  00:44.45 11/2   6    298+  169M+  48K    19M    1806
0     kernel_task  24.6  03:56.36 479/8  0    0     7808K  0B     0B     0
570   com.docker.b 10.7  00:27.26 26     1    113   37M    0B     7968K  570
742   com.docker.v 10.1  00:31.88 5      0    18    30M    0B     3376K  570
744   vpnkit-bridg 9.7   00:26.95 13     0    33    13M    0B     2224K  570
745   com.docker.d 9.4   00:29.97 16     1    53    16M+   0B     2208K  570
2781  QQMusic      7.4   01:24.35 55     5    748   151M-  144K+  34M    2781
2097  java         5.9   00:43.45 64     5    414   1368M  64K    41M    2091
1030  idea         4.9   07:33.49 75/3   3    543   2177M  64K    5771M- 1030


----- 500 PQS[实际375+，没达到] ---------
Processes: 473 total, 11 running, 462 sleeping, 2832 threads           09:18:37
Load Avg: 14.35, 7.83, 5.28  CPU usage: 50.5% user, 30.17% sys, 19.76% idle
SharedLibs: 440M resident, 90M data, 107M linkedit.
MemRegions: 259603 total, 5212M resident, 566M private, 2695M shared.
PhysMem: 15G used (1786M wired), 102M unused.
VM: 189T vsize, 3272M framework vsize, 0(0) swapins, 0(0) swapouts.
Networks: packets: 11615277/6586M in, 12213415/6676M out.
Disks: 807888/15G read, 190889/4024M written.

PID   COMMAND      %CPU  TIME     #TH    #WQ  #PORT MEM    PURG   CMPRS  PGRP
2692  java         318.6 29:26.36 93     1    255   451M   0B     58M    2692
749   qemu-system- 144.3 32:06.70 10/4   0    25    8883M  0B     665M   570
0     kernel_task  29.5  07:47.21 479/9  0    0     7760K  0B     0B     0
144   WindowServer 25.0  09:12.32 21     5    3843  636M+  8896K  120M   144
1806  Terminal     21.6  02:31.15 8      3    286-  504M+  48K    304M   1806
570   com.docker.b 14.4  01:56.21 26/2   1    115   29M    0B     4000K  570
744   vpnkit-bridg 13.5  01:57.09 14/3   0    34    13M    0B     2320K  570
745   com.docker.d 13.4  02:12.15 17/2   1    54    18M    0B     3952K  570
742   com.docker.v 12.8  02:19.98 5/1    0    18    32M    0B     4576K  570
2097  java         7.4   01:42.57 93     4    471   1695M  384K   71M    2091
2781  QQMusic      6.5   03:22.13 55     5    905-  166M+  448K-  73M    2781
1030  idea         3.4   08:28.09 75/1   3    555   2190M  64K    4955M- 1030
5197  top          3.2   00:01.80 1/1    0    26    5249K  0B     0B     5197
2911  bluetoothaud 1.7   00:41.80 4      1    175   6130K  0B     2368K  2911



```





## IO 查看 



```
iostat 1

----- 0 PQS ---------
              disk0       cpu    load average
    KB/t  tps  MB/s  us sy id   1m   5m   15m
   22.62  277  6.12  12  9 79  1.50 4.13 3.53

----- 50 PQS ---------

              disk0       cpu    load average
    KB/t  tps  MB/s  us sy id   1m   5m   15m
   22.56  256  5.65  13 10 77  4.00 4.82 4.13
    9.91   15  0.15  14 16 70  4.00 4.80 4.13
   12.21   19  0.23  13 16 71  4.00 4.80 4.13
   22.76   75  1.67  14 15 71  4.00 4.79 4.13
   15.94   22  0.34  16 16 69  4.00 4.78 4.13
   42.40   80  3.31  17 17 66  4.00 4.78 4.13
   24.36   15  0.36  15 17 68  4.00 4.76 4.13
   46.84   10  0.47  15 17 68  4.00 4.76 4.13
   19.04  337  6.26  17 18 65  3.84 4.72 4.12
   13.49   12  0.15  18 16 67  3.69 4.67 4.10
   12.32  136  1.64  21 17 62  3.69 4.67 4.10
   20.38   35  0.69  18 15 66  3.56 4.63 4.09
   12.09   15  0.18  18 16 66  3.56 4.63 4.09
   22.87   24  0.53  17 18 65  3.51 4.60 4.09
   17.23    9  0.15  13 16 71  3.39 4.56 4.07
   22.67    9  0.20  15 17 68  3.39 4.56 4.07
   
   
----- 200 PQS ---------
              disk0       cpu    load average
    KB/t  tps  MB/s  us sy id   1m   5m   15m
   22.61  271  5.98  12  9 79  6.14 4.69 3.78
   13.31   21  0.28  31 25 44  6.14 4.69 3.78
   16.00   22  0.34  33 26 42  6.77 4.85 3.84
   16.12   11  0.17  32 26 42  7.10 4.95 3.89
   19.78   12  0.24  35 28 37  7.10 4.95 3.89
   23.61  473 10.90  35 28 36  7.74 5.12 3.95
    9.62   32  0.30  33 28 39  7.74 5.12 3.95
   13.13   33  0.42  34 27 39  7.60 5.13 3.96
   47.14   93  4.30  35 26 39  8.75 5.41 4.07
   27.19   41  1.09  36 27 38  8.75 5.41 4.07
   24.96   25  0.61  33 27 40  9.17 5.55 4.13
   15.83   16  0.24  33 28 39  9.17 5.55 4.13
   14.21   16  0.22  33 27 40  9.24 5.63 4.16
   26.21  114  2.92  34 26 40  10.66 5.98 4.29
   

   
----- 500 PQS[实际375+，没达到] ---------

              disk0       cpu    load average
    KB/t  tps  MB/s  us sy id   1m   5m   15m
   19.85  265  5.14  13 11 76  7.81 4.32 3.84
   28.42   57  1.57  52 29 19  7.81 4.32 3.84
   18.58   35  0.63  51 30 19  8.79 4.58 3.93
   17.02  211  3.51  54 29 17  8.79 4.58 3.93
   20.82  403  8.19  53 30 17  8.89 4.67 3.97
   16.51   24  0.38  53 31 16  9.30 4.83 4.02
   27.95   58  1.57  50 30 20  9.30 4.83 4.02
   19.89   59  1.14  54 30 16  9.11 4.86 4.04
   30.26   38  1.13  52 29 19  9.11 4.86 4.04
   16.23   18  0.28  52 30 19  9.66 5.05 4.11
   16.30  115  1.83  53 30 18  9.69 5.13 4.15
    8.30   57  0.46  52 30 18  9.69 5.13 4.15
   30.45   85  2.52  53 29 18  10.11 5.29 4.21
   56.89   66  3.66  52 30 18  10.11 5.29 4.21
   19.51  555 10.58  53 31 16  10.75 5.50 4.29


```





## GC日志

https://zhuanlan.zhihu.com/p/615616580



## VIVO 案例

https://zhuanlan.zhihu.com/p/568968541