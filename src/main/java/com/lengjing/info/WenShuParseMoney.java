package com.lengjing.info;

import com.lengjing.info.util.ParseUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.ReflectionUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hive.com.esotericsoftware.minlog.Log;


public class WenShuParseMoney {
    private static SequenceFile.Reader reader = null;
    private static Configuration conf = new Configuration();

    public static class WenShuMapper extends Mapper<Object,Text,Text,Text>{
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String [] arr=value.toString().split("\u0001");
            Log.info("judgmentid:"+arr[0]);
            if(arr.length>=4) {
               /* Log.info("value"+value.toString());
                ;*/

                String judgmentid = arr[0];
                String yuangao = arr[1];
                String beigao = arr[2];
                String jg = arr[3];
                Text t = new Text(judgmentid);
                Text rtext=new Text();
                Log.info("beigao:"+beigao);
               try{
                String result = ParseUtil.parseMoney(jg,yuangao,beigao);
                String brr[] = result.split("_");
                    if(brr.length>=2){
                        String victory="";
                        if(beigao!=null && !beigao.equals("")){
                            if (brr[1].equals("被告")) {
                                victory = beigao;
                            } else {
                                victory = yuangao;
                            }
                        }else {
                            victory=brr[1];
                        }
                        rtext.set(judgmentid + "\u0001" + yuangao + "\u0001" + beigao + "\u0001" + jg + "\u0001" + brr[0] + "\u0001" + victory);
                    }else{
                        rtext.set(judgmentid+"\u0001"+yuangao + "\u0001" + beigao + "\u0001" +jg+"\u0001"+ brr[0] + "\u0001" + "");
                    }
                    context.write(t, rtext);
                }catch (Exception e){
                    rtext.set(judgmentid+"\u0001"+yuangao + "\u0001" + beigao + "\u0001"+jg+"\u0001" + "error" + "\u0001" + "fail");
                    context.write(t, rtext);

               }

            }
        }
    }
    public static class WenShuReduce extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text arg0, Iterable<Text> arg1,
                              Reducer<Text, Text, Text, Text>.Context arg2)
                throws IOException, InterruptedException {
            // TODO Auto-generated method stub
            for (Iterator<Text> iter = arg1.iterator(); iter.hasNext();) {
                arg2.write(arg0, iter.next());
            }
        }

    }
}
