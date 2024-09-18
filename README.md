#  这个项目是干什么的
- 提供单体spring-boot脚手架，进行快速开发。对于每次新建项目时，建包、日志、日志格式文件、数据库配置等等...通用功能的搭建太耽误我们的时间。 拉取下来修改配置信息，就搭建好了基础框架可以直接使用。
- 具体依赖请查看`pom.xml`
- 集成了目前比较常用的Spring Boot相关依赖，编写单体项目最好的脚手架

## 特殊说明

1. 项目中能使用并带有`Enable`的注解都已经加在启动类上
2. 所有的业务代码写在domain包里面

## 包介绍

### application模块
```text
└───com
    └───aks
        └───scaffold
            ├───aspectj 接口日志
            ├───config  配置
            ├───constant 常量
            ├───controller
            │   ├───exception 全局异常拦截
            ├───domain
            │   └───common
            │       ├───entity 基础实体类
            │       ├───mapper 基础mapper
            │       └───service 基础service
            ├───filter
            └───toolkit
                ├───page 分页
                └───redis redis工具类
```
### sdk模块
```text
└───com
    └───aks
        └───sdk
            ├───exception 自定义异常
            ├───model 通用模型
            ├───resp 响应模型
            └───util 
                ├───asserts 断言工具
                ├───file 文件相关工具类
                ├───jwt jwt工具类
                ├───md5 md5工具类
                └───thread 线程工具类
```
