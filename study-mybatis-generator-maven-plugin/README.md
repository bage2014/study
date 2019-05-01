# study-mybatis-generator-maven-plugin  #
学习mybatis-generator-maven-plugin的笔记<br>

## 参考链接 ##

maven 依赖 [http://www.mybatis.org/generator/running/runningWithMaven.html](http://www.mybatis.org/generator/running/runningWithMaven.html)

配置文件 [http://www.mybatis.org/generator/configreference/xmlconfig.html](http://www.mybatis.org/generator/configreference/xmlconfig.html)

## 生成步骤 ##

### 修改配置 ###
- 修改jar仓库路径

- 修改数据源配置

- 修改domain、mapper、xml配置

### mvn命令 ###

    mvn mybatis-generator:generate

## 注意事项 ##

- mysql-connector-java 驱动版本可能要进行调整

- xxxByExample 可以设置为 false
