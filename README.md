#  这个项目是干什么的
- 提供单体spring-boot脚手架，进行快速开发。对于每次新建项目时，建包、日志、日志格式文件、数据库配置等等...通用功能的搭建太耽误我们的时间。 拉取下来修改配置信息，就搭建好了基础框架可以直接使用。
- 具体依赖请查看`pom.xml`
- 集成了目前比较常用的Spring Boot相关依赖，编写单体项目最好的脚手架
- 其实这个仓库更像是一个web项目，但是没有业务在其中。拉取本项目到本地打开其中有样例说明

## 特殊说明

1. 项目中能使用并带有`Enable`的注解都已经加在启动类上
2. 所有的业务代码写在domain包里面

## 包介绍

### application模块
```text
├───java
│   └───com
│       └───aks
│           └───scaffold
│               ├───config   MVC常用配置如Token、Redis配置
│               ├───controller
│               │   └───exception  全局异常捕获
│               ├───domain 业务层可以自己创建相关的mvc文件夹
│               │   └───common
│               │       ├───entity 通用实体类
│               │       ├───mapper 基于MyBatisPlus的BaseMapper扩展的Mapper
│               │       └───service 基于MyBatisPlus的IService扩展的Service
│               ├───filter  设置UTF
│               └───toolkit  
│                   ├───aop 全局接口、异常打印
│                   ├───page 分页请求工具类
│                   └───redis redis工具类
```
### sdk模块
```text
└───com
    └───aks
        └───sdk
            ├───exception  异常类
            ├───log    异步日志工具类
            ├───resp    统一响应类
            └───util 
                ├───asserts 断言工具类
                ├───file 文件工具类
                ├───jwt jwt工具类
                ├───md5 MD5工具类
                └───thead 线程工具类
```
