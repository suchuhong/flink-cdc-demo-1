package org.example;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * 时区很重要
 * https://blog.csdn.net/watson2017/article/details/139451432
 * 需要进入容器设置
 *https://hub.docker.com/layers/library/mysql/8.0.40/images/sha256-1c9a74f109e3f0652a74e26835d483cf43412611a0c9895e03ee6694a6703698?context=explore
 * https://dev.mysql.com/doc/refman/8.4/en/show-master-status.html
 */
public class CdcTest {
    public static void main(String[] args) throws Exception {
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("localhost")
                .port(3302)
                .databaseList("flink_demo")
                .tableList("flink_demo.orders")
                .username("root")
                .password("rootpassword")
                .serverTimeZone("Asia/Shanghai")
                .deserializer(new JsonDebeziumDeserializationSchema())
                .includeSchemaChanges(true)
                .build();


        // web ui port
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8881);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);

        // enable checkpoint
        env.enableCheckpointing(3000);

        env
                .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
                .setParallelism(4)
                .addSink(new CustomSink());

        env.execute("Print MySQL Snapshot + Binlog");
    }
}