#!/bin/sh

echo '---------------开始自动部署---------------'

# 项目根目录
ROOT_PATH=$(cd $(dirname $0);cd .; pwd)

# 项目名称
PROJECT_NAME=ChatGPT

# LINUX部署目录
DEPLOY_PATH=root/ChatGPT

# 服务器地址
SERVER_IP=xxx.xx.xx.xx

echo '---------------项目本地路径---------------'

echo ${ROOT_PATH} && cd ${ROOT_PATH}

echo '---------------开始项目打包---------------'

mvn clean package -Dmaven.test.skip=true

mv target/${PROJECT_NAME}-0.0.1-SNAPSHOT.jar target/${PROJECT_NAME}.jar

echo '---------------开始上传项目---------------'

chmod -R 700 id_rsa.pem

scp -r -i id_rsa.pem -P 22 target/${PROJECT_NAME}.jar root@${SERVER_IP}:/${DEPLOY_PATH}

echo '---------------kill源进程---------------'

ssh -i id_rsa.pem -p 22 root@${SERVER_IP} << EOF

cd /${DEPLOY_PATH};

ps -ef | grep ${PROJECT_NAME}.jar | grep -v 'grep' | cut -c 9-15 | xargs kill -s 9

echo '---------------启动项目中---------------'

chmod +x start.sh

chmod +x stop.sh

./start.sh

tail -f -n 50 app.log

EOF