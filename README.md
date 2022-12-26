# OpenAI聊天服务 - ChatGPT

## 服务在线使用及开发环境

> https://qqil.cn
>
> JDK 8 + IDEA 2021

## 开发

* 确保java8及Maven环境，克隆或下载项目后通过IDEA打开项目。
* 配置application.yml中的`secret-key`。
* 打包命令`mvn clean package`。

Linux部署后可通过BT反代：
```bash
nohup java -jar -Xmx200m ChatGPT.jar >> ChatGPT.log  &
```

## 注意

该网站不对用户填写内容做任何存储，如果有能力请自行部署！
OpenAI的secret-key获取教程可以去Bilibili搜搜。

## 功能

- [x] 聊天
- [x] 代码
- [ ] 接口限流

待更新更多功能...




