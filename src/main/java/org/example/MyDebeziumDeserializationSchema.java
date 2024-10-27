package org.example;


import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

// 自定义反序列化器，将 CDC 事件转换为 SQL 语句
public class MyDebeziumDeserializationSchema implements DebeziumDeserializationSchema<String> {
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<String> collector) throws Exception {
        Struct value = (Struct) sourceRecord.value();
        Struct source = value.getStruct("source");

        // 获取变更类型 (INSERT, UPDATE, DELETE)
        String operation = Envelope.operationFor(sourceRecord).toString().toLowerCase();

        Struct after = value.getStruct("after");
        Struct before = value.getStruct("before");

        String sql = "";

        switch (operation) {
            case "create":  // INSERT 语句
                sql = buildInsertSQL(after);
                break;
            case "update":  // UPDATE 语句
                sql = buildUpdateSQL(before, after);
                break;
            case "delete":  // DELETE 语句
                sql = buildDeleteSQL(before);
                break;
        }

        collector.collect(sql);  // 将 SQL 语句收集并输出
    }

    // 构建 INSERT 语句
    private String buildInsertSQL(Struct after) {
        return String.format("INSERT INTO orders (order_id, customer_name, total_amount) VALUES (%d, '%s', %.2f);",
                after.get("order_id"),
                after.get("customer_name"),
                after.get("total_amount"));
    }

    // 构建 UPDATE 语句
    private String buildUpdateSQL(Struct before, Struct after) {
        return String.format("UPDATE orders SET customer_name='%s', total_amount=%.2f WHERE order_id=%d;",
                after.get("customer_name"),
                after.get("total_amount"),
                before.get("order_id"));
    }

    // 构建 DELETE 语句
    private String buildDeleteSQL(Struct before) {
        return String.format("DELETE FROM orders WHERE order_id=%d;", before.get("order_id"));
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return TypeInformation.of(String.class);
    }


}