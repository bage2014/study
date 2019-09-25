# study-lombok #
lombok 学习笔记

## 环境搭建 ##
### 安装IDE插件 ###
IDEA 插件 [https://github.com/mplushnikov/lombok-intellij-plugin](https://github.com/mplushnikov/lombok-intellij-plugin)

- 可采用离线安装方式，从上面链接进行下载，最后进行导入
- 也可以采用在线安装方式进行安装 lombok 插件

### 添加依赖 ###
之后再项目中，添加lombok依赖即可使用

	<dependency>
	    <groupId>org.projectlombok</groupId>
	    <artifactId>lombok</artifactId>
	    <version>1.16.18</version>
	    <scope>provided</scope>
	</dependency>

### 基本使用 ###

- @Getter and @Setter
生成set、get方法

- @ToString
生产 toString方法

- @EqualsAndHashCode


- @AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
- @Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger
- @Data
- @Builder
- @Singular
- @Delegate
- @Value
- @Accessors
- @Wither
- @SneakyThrows
- @val available from IntelliJ 14.1 (improved in 2016.2)
- @UtilityClass available from IntelliJ 2016.2



