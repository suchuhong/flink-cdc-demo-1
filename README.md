# flink-cdc-demo

## mysql 时区

mysql docker-compose 配置文件

```shell

services:
  mysql:
    image: mysql:8.0.40  # 使用 MySQL 8.0 镜像
    container_name: mysql-container  # 容器名称
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword  # MySQL root 用户的密码
      MYSQL_DATABASE: mydatabase  # 初始化时创建的数据库名称
      MYSQL_USER: user  # 创建的普通用户
      MYSQL_PASSWORD: userpassword  # 普通用户的密码
      TZ: Asia/Shanghai  # 设置时区
    ports:
      - "3302:3306"  # 映射 MySQL 端口
    volumes:
      - ./db_data:/var/lib/mysql  # 数据持久化
      # - ./my.cnf:/etc/my.cnf  # 将本地配置文件挂载到容器中



```

容器内修改时区

```
mysql> SELECT @@global.time_zone, @@session.time_zone;
+--------------------+---------------------+
| @@global.time_zone | @@session.time_zone |
+--------------------+---------------------+
| SYSTEM             | SYSTEM              |
+--------------------+---------------------+
1 row in set (0.01 sec)

mysql> SET time_zone = '+8:00';
Query OK, 0 rows affected (0.22 sec)

mysql> SET GLOBAL time_zone = '+8:00';
Query OK, 0 rows affected (0.00 sec)

mysql> SELECT @@global.time_zone, @@session.time_zone;
+--------------------+---------------------+
| @@global.time_zone | @@session.time_zone |
+--------------------+---------------------+
| +08:00             | +08:00              |
+--------------------+---------------------+
1 row in set (0.00 sec)
```

## mysql 版本

https://nightlies.apache.org/flink/flink-cdc-docs-master/zh/docs/connectors/flink-sources/mysql-cdc/

MySQL: 5.6, 5.7, 8.0.x

## mysql 读取 binlog 装换为 sql

## web ui 

http://localhost:8881/#/job

### 查看错误日志


