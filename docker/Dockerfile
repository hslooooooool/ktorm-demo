# 基于 JAVA 8 镜像
FROM java:8

# 将本地文件夹挂载到当前容器
VOLUME /tmp

# 拷贝文件到容器
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

# 设置时区
RUN bash -c ‘echo “Asia/Shanghai” > /etc/timezone’

# 开放8081端口
EXPOSE 8081

# 设置JVM 内存限制 512M
ENV JAVA_OPTS="-Xmx512m"

# 配置容器启动后执行的命令
ENTRYPOINT ["java","${JAVA_OPTS}","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]