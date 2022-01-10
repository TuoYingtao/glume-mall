# glume-mall微服务平台简介
glume-mall是一个B2C模式的电商平台，销售自营商品给客户。采用前后端分离的模式：开发基于Vue的后台管理系统，前端开源框架[Vue](https://cn.vuejs.org/v2/guide/)、[RuoYi](https://gitee.com/y_project/RuoYi-Vue) 、 [Element UI 2.15.5](https://element.eleme.cn/#/zh-CN/component/installation)，后端开发框架 [SpringBoot 2.2.2](https://spring.io/projects/spring-boot)整合[SpringCloud H](https://spring.io/projects/spring-cloud)、[MyBatisPlus 3.3.1](https://baomidou.com/)、[Redis](https://redis.io/)、MySql V8.0.25等，使用人人开源项目的代码生成器在线生成entity、xml、dao、service。

## 技术架构

**开发工具**

* IDEA 安装Lombok插件

**开发环境**

* 语言：Java 1.8.0_261
* 依赖管理：Maven 3.8.1
* 数据库：MySQL 8.0.25
* 缓存：Redis 5.0.10
* 服务注册与配置中心：Nacos 2.0.3

**后端**

* 基础框架：  [SpringBoot 2.2.2.RELEASE](https://spring.io/projects/spring-boot)
* 微服务框架：[SpringCloud Hoxton.SR1](https://spring.io/projects/spring-cloud)、[SpringCloudAlibaba 2.1.0.RELEASE](https://spring.io/projects/spring-cloud-alibaba)
* 远程调用：[OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
* 网关：[Gateway](https://spring.io/projects/spring-cloud-gateway)
* 持久层框架：[MyBatisPlus 3.3.1](https://baomidou.com/)
* 缓存框架：[Redis](https://redis.io/)、[Redisson](https://redis.io/topics/distlock)分布式锁、[Spring Cache](https://docs.spring.io/spring-framework/docs/current/reference/html/)
* 安全框架：[SpringSecurity](https://spring.io/projects/spring-security)、JWT 0.9.1
* 日志：slf4j
* 其它：[fastjson 1.2.76](https://github.com/alibaba/fastjson/wiki)、lombok 1.18.10、[hutool 5.3.10](https://www.hutool.cn/docs/#/)、validation 2.0.1