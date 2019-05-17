# 基于 JAVA 8 镜像
FROM java:8

# 将本地文件夹挂载到当前容器
VOLUME /tmp

# 拷贝文件到容器
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

# 开放8081端口
EXPOSE 8081

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]