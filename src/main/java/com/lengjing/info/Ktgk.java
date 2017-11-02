package com.lengjing.info;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hive.com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class Ktgk {
    public static class DeleteMapper extends Mapper<Object, Text, NullWritable, NullWritable> {

        HTableInterface htable;

        protected void setup(
                Mapper<Object, Text, NullWritable, NullWritable>.Context context)
                throws IOException, InterruptedException {
            // TODO Auto-generated method stub
            Configuration conf = new Configuration();
            conf.set("hbase.zookeeper.quorum", "hadoop1,hadoop2,hadoop3");
            conf.set("hbase.zookeeper.property.clientPort", "2181");
            String tableName = "Dsr2SiFaDoc";
            htable = new HTable(conf, tableName);
            htable.setAutoFlushTo(false);
            htable.setWriteBufferSize(6 * 1024 * 1024);
        }

        @Override
        protected void map(Object key, Text value,
                           Mapper<Object, Text, NullWritable, NullWritable>.Context context)
                throws IOException, InterruptedException {
            // TODO Auto-generated method stub
            String arr[] = value.toString().split("\t", -1);
            Delete d = new Delete(Bytes.toBytes(arr[0]));
            Log.info("message:"+arr[0]);
            htable.delete(d);
        }

        public void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
            htable.flushCommits();
            htable.close();

        }
    }
}
