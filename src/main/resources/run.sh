#!/bin/sh

# ------------------------
# author：Javis
# 服务执行脚本
# ------------------------

# 所在目录
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# 应用名
APP_NAME=@project.build.finalName@
# 运行环境 开发环境=dev 测试环境=test 生产环境=prod
ENV=@project.active@
# 端口
PORT=10009
# 配置中心地址
CONFIG_URI=http://localhost:10010
# 注册中心host
CONSUL_HOST=localhost
# 注册中心端口
CONSUL_PORT=8500
# JVM参数
JVM_OPT="-Xms128m -Xmx128m"
# 系统属性
SYS_OPT="-Dloader.path=lib/ -DSpring.profiles.active=$ENV -Dspring.cloud.config.uri=$CONFIG_URI -Dspring.cloud.consul.host=$CONSUL_HOST -Dspring.cloud.consul.port=$CONSUL_PORT"
# Spring属性
SPRING_OPT="--server.port=$PORT --temp=03"

# 使用说明
usage() {
  echo "usage: run.sh start|stop|restart|status"
}

case "$1" in

"start")
  echo "starting $APP_NAME service"
  echo "nohup java $SYS_OPT -jar $JVM_OPT $DIR/$APP_NAME.jar $SPRING_OPT >/dev/null 2>&1 &"
  nohup java $SYS_OPT -jar $JVM_OPT $DIR/$APP_NAME.jar $SPRING_OPT >/dev/null 2>&1 &
  pid=$(lsof -i:"$PORT" | grep "LISTEN" | awk '{print $2}')
  #  until [ -n "$pid" ]; do
#    pid=$(lsof -i:"$PORT" | grep "LISTEN" | awk '{print $2}')
#  done
  echo "$APP_NAME Started on port $PORT. pid=$pid"
  ;;

"restart")
  $0 stop
  $0 start

  ;;

"stop")
  echo "closing $APP_NAME service"
  pid=$(ps -ef | grep -w $APP_NAME | grep -v "grep" | awk '{print $2}')
  if [ "$pid" = "" ]; then
    echo "$APP_NAME Process not exists or stop success"
  else
    kill -9 $pid
    echo "$APP_NAME Killed success pid=$pid port=$PORT"
  fi
  ;;

"status")
  pid=$(ps -ef | grep -w $APP_NAME | grep -v "grep" | awk '{print $2}')
  if [ "$pid" = "" ]; then
    echo "$APP_NAME is inactivated"
  else
    echo "$APP_NAME is actived pid=$pid"
  fi
  ;;

"log")
  tail -f $DIR/logs/$APP_NAME.log
  ;;

*)
  usage
  ;;

esac
