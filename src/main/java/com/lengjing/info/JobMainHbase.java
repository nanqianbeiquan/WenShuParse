package com.lengjing.info;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileAsTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.IOException;


public class JobMainHbase {
    public  static void  main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        Job job = new Job(conf, "hbase");
        job.setJarByClass(JobMain.class);
        job.setMapperClass(WenShuParseHbase.ImportHbase.class);
        job.setNumReduceTasks(0);
        job.setInputFormatClass(SequenceFileAsTextInputFormat.class);
        job.setOutputFormatClass(NullOutputFormat.class);
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        job.waitForCompletion(true);

       /* Counters counters=job.getCounters();
        counters.findCounter(LOG_PROCESSOR_COUNTER.)*/
    }
}

