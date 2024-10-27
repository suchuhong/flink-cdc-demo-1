package org.example;

import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class CustomSink extends RichSinkFunction<String> {
    @Override
    public void invoke(String value, Context context) throws Exception {
        System.out.println(">>> " + value);
//        输出sql
//        迁移数据
    }

    @Override
    public void writeWatermark(Watermark watermark) throws Exception {
        super.writeWatermark(watermark);
    }

    @Override
    public void finish() throws Exception {
        super.finish();
    }
}
