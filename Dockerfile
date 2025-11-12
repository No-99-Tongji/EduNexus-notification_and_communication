# 多阶段构建 - 构建阶段
FROM maven:3.8.8-eclipse-temurin-17-alpine AS build

# 设置工作目录
WORKDIR /app

# 复制pom.xml和源代码
COPY pom.xml .
COPY src ./src

# 构建应用（跳过测试以加快构建速度）
RUN mvn clean package -DskipTests

# 运行阶段 - 使用更小的JRE镜像
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 创建非root用户运行应用
RUN addgroup -S spring && adduser -S spring -G spring

# 从构建阶段复制jar文件
COPY --from=build /app/target/*.jar app.jar

# 更改文件所有者
RUN chown spring:spring app.jar

# 切换到非root用户
USER spring:spring

# 暴露应用端口
EXPOSE 8081

# 设置JVM参数和时区
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Duser.timezone=Asia/Shanghai"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

