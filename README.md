# Forum
A website for asking questions and getting answers. Version 1.0.0
访问地址：http://www.wujiahui.net/forum

## 1、网站介绍
该网站实现了提问，评论，注册登录，未登录跳转，记住登录状态，私聊，搜索，系统通知等功能。

## 2、工具的使用
利用 Spring Boot生成项目框架，利用 Spring、Spring MVC、MyBatis 搭建后台服务。

用 MySQL 作为主要数据库；用 Redis 存验证码、登录状态等时效性短的数据，
同时为部分存在 MySQL 的数据作缓存。

使用 Kafka 作为消息队列，以异步处理特定事件，提高性能。

使用 Elasticsearch 来提供搜索服务。

## 3、代码导航
前端模板：[src/main/resources](https://github.com/NgCafai/forum/tree/master/src/main/resources)

后端代码：[src/main/java/com/wujiahui/forum](https://github.com/NgCafai/forum/tree/master/src/main/java/com/wujiahui/forum)
