# OpenAI聊天服务 - ChatGPT

## 服务在线使用及开发环境

> https://chat.rawchen.com
>
> JDK 8 + IDEA 2023.2

## 开发

* 确保java8及Maven环境，克隆或下载项目后通过IDEA打开项目。
* 配置application.yml中的`secret-key`。
* 配置password.txt密码访问。
* 打包命令`mvn clean package`。

Linux部署：
```bash
./remote_deploy.sh
```
或者上传打包jar文件和start.sh/stop.sh，执行：
```bash
chmod +x start.sh
./start.sh
```

## 注意

该网站不对用户填写内容做任何存储，如果有能力请自行部署！
OpenAI的secret-key获取通过第三方: [https://beta.theb.ai](https://beta.theb.ai)。

## 功能

- [x] 与最先进的模型聊天
- [x] 一款强大的AI搜索引擎
- [ ] 强大的图像生成
- [x] 密码访问
- [x] MD渲染
- [ ] 接口限流

待更新更多功能...

## 截图

![Search](https://rawchen.com/temp/chatgpt01.png)

![Chat](https://rawchen.com/temp/chatgpt02.png)
