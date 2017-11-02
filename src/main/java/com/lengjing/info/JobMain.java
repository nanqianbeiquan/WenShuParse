package com.lengjing.info;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.conf.Configuration;
import java.io.IOException;

import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class JobMain {
    public  static void  main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        Job job = new Job(conf,"wenshuparse");
        job.setJarByClass(JobMain.class);
        job.setMapperClass(WenShuParseMoney.WenShuMapper.class);
        job.setReducerClass(WenShuParseMoney.WenShuReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        Path path=new Path(args[0]);
        FileInputFormat.addInputPath(job, path);
        FileSystem fileSystem = FileSystem.get(new Configuration());
        Path out=new Path(args[1]);
        if (fileSystem.exists(out)) {
            fileSystem.delete(out, true);
        }
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        try {
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*Configuration conf = HBaseConfiguration.create();
        Job job = new Job(conf, "ktgg");
        job.setJarByClass(JobMain.class);
        job.setMapperClass(Ktgk.DeleteMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(NullOutputFormat.class);
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        job.waitForCompletion(true);*/
    }
}

