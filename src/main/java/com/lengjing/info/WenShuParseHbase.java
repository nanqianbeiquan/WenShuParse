package com.lengjing.info;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hive.com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class WenShuParseHbase {
    public static class ImportHbase extends Mapper<Object,Text,Text,Text>{
        public HTable htable=null;
        public void setup(Context context) throws IOException, InterruptedException
        {
            Configuration conf=new Configuration();
            conf.set("hbase.zookeeper.quorum", "hadoop1,hadoop2,hadoop3");
            conf.set("hbase.zookeeper.property.clientPort", "2181");
            htable=new HTable(conf,"wenshu_parse");
            htable.setAutoFlushTo(false);
            htable.setWriteBufferSize(6*1024*1024);
        }

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String arr[]=value.toString().split("\u0001");
           String rowkey=arr[0];
           String yuangao=arr[1];
           String beigao=arr[2];
           String paijuejieguo=arr[3];
           String money=arr[4];
           String victory="";
           if(arr.length>=6) {
                victory = arr[5];
           }
            Log.info("message:"+rowkey);
           Put p=new Put(Bytes.toBytes(rowkey));
            p.add(Bytes.toBytes("ws"),Bytes.toBytes("yuangao"),Bytes.toBytes(yuangao));
            p.add(Bytes.toBytes("ws"),Bytes.toBytes("beigao"),Bytes.toBytes(beigao));
            p.add(Bytes.toBytes("ws"),Bytes.toBytes("paijuejieguo"),Bytes.toBytes(paijuejieguo));
            p.add(Bytes.toBytes("ws"),Bytes.toBytes("money"),Bytes.toBytes(money));
            p.add(Bytes.toBytes("ws"),Bytes.toBytes("loser"),Bytes.toBytes(victory));
            htable.put(p);
        }
        public void cleanup(Mapper.Context context) throws IOException, InterruptedException
        {
            super.cleanup(context);
            htable.flushCommits();
            htable.close();
        }
    }

}
