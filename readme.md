# Web应用框架 - SpringBoot MyBatis  

当前版本:1.0.0  

## 项目简介  

基于springboot的web应用框架，使用MyBatis作为数据持久层，利用velocity生成controller/service/dao/mapper.xml文件，简化开发效率


## 技术选型  

### 技术栈  

使用前后分离的开发模式，后端只提供接口给前台调用，接口安全采用Json web token (JWT)机制，即基于token的鉴权机制。  

### 后端框架  

| 技术 | 名称 | 网址 |
| :--- | :--- | :--- |
| Spring Framework | 容器 | [http://projects.spring.io/spring-framework](http://projects.spring.io/spring-framework/) |
| Spring Boot | 整合框架 | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| Spring MVC | MVC框架 | [https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) |
| MyBatis | 持久层 | [https://blog.mybatis.org/](https://blog.mybatis.org/) |
| Logback | 日志组件 | [https://logback.qos.ch](https://logback.qos.ch) |
| Maven | 项目构建 | [http://maven.apache.org](http://maven.apache.org/) |
| Druid | 数据库连接池 | [https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98](https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98) |
| Hutool | 工具类 | [http://hutool.mydoc.io](http://hutool.mydoc.io) |
| Redis | 分布式缓存数据库 | [https://redis.io](https://redis.io) |
| MySQL | 关系数据库 | [https://www.mysql.com](https://www.mysql.com) |
| Velocity | 模板引擎 | [http://velocity.apache.org/](http://velocity.apache.org/) |
  
  
## 第三方平台  

| 名称 | 平台 | 网址 |
| :--- | :--- | :--- |
| xxx | xxxx | xxx |
  

## 开发环境  
建议开发者使用以下环境，这样避免版本带来的问题  
* IDE:IDEA
* DB:MySQL5.7
* JDK:JAVA 8
* WEB:Tomcat8
  
## 运行环境  

* WEB服务器：Weblogic、Tomcat、WebSphere、JBoss、Jetty 等
* 数据库服务器：Mysql5.7
* 操作系统：Windows、Linux、Unix 等


## 项目部署  

在所开发项目的根路径下，进入命令行，输入：  
```
mvn package -DskipTests
```

即可将项目打包成可部署到tomcat下的war文件，将war文件拷贝到tomcat下的webapps目录中，在tomcat下的bin目录中运行./startup.sh启动tomcat即可

  
## 系统结构  

基于技术分包，早年的Java分包方式通常是基于技术的，比如与domain包平级的有controller包、service包和mapper包等。

## 文件说明  

* src/main/java java 源代码
* src/main/java/com/Application.java 程序入口
* src/main/resources 项目的资源配置文件
* README.md 项目说明文档
* pom.xml 依赖配置文件

## 编码实践  

统一的编码实践，比如异常处理原则、分页封装等

### 异常处理  

当前端调用后台接口有类似数据不符合规范（比如：字段为必输项，找不到关联对象）时，可抛出BusinessException("异常信息")，系统会将该异常信息返回给前端。


## 资料文档  


## FAQ  

开发过程中常见问题的解答  


## 引用  

本项目部分功能参考ruoyi的设计，特此鸣谢 http://www.ruoyi.vip/index