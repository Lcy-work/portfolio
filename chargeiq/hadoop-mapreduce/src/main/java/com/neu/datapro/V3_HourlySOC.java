package com.neu.datapro;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** V3 - 每时电池平均SOC */
public class V3_HourlySOC {
    public static class M extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable k, Text v, Context c) throws IOException, InterruptedException {
            String[] f = v.toString().split(",");
            if (f.length < 3 || f[2].equals("0.00")) return;
            String h = (f[1].length()>=10) ? f[1].substring(8,10) : f[1];
            c.write(new Text(h), new Text(f[2]));
        }
    }
    public static class R extends Reducer<Text, Text, Text, Text> {
        private Connection conn; private PreparedStatement ps;
        public void setup(Context c) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/enger","root","root");
                conn.prepareStatement("DELETE FROM t_enger3").executeUpdate();
                ps = conn.prepareStatement("INSERT INTO t_enger3 (record_hour,avg_soc) VALUES(?,?)");
            } catch (SQLException e) { throw new RuntimeException(e); }
        }
        public void reduce(Text k, Iterable<Text> vs, Context c) throws IOException, InterruptedException {
            int n=0; double t=0;
            for (Text v:vs) { t+=Double.parseDouble(v.toString()); n++; }
            try { ps.setString(1,k.toString()); ps.setDouble(2,t/n); ps.executeUpdate(); }
            catch (SQLException e) { throw new RuntimeException(e); }
            c.write(k, new Text(String.valueOf(t/n)));
        }
        public void cleanup(Context c) { try { if(ps!=null)ps.close(); if(conn!=null)conn.close(); } catch (Exception e) {} }
    }
    public static void main(String[] a) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + (a.length>0?a[0]:"localhost") + ":9000");
        Job j = Job.getInstance(conf,"V3"); j.setJarByClass(V3_HourlySOC.class);
        j.setMapperClass(M.class); j.setMapOutputKeyClass(Text.class); j.setMapOutputValueClass(Text.class);
        j.setReducerClass(R.class); j.setOutputKeyClass(Text.class); j.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(j, new Path(a.length>1?a[1]:"/Car/dsv13r1.csv"));
        FileOutputFormat.setOutputPath(j, new Path(a.length>2?a[2]:"/Car/v3_out"));
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(new Path(a.length>2?a[2]:"/Car/v3_out"))) fs.delete(new Path(a.length>2?a[2]:"/Car/v3_out"), true);
        System.out.println("V3 running...");
        System.exit(j.waitForCompletion(true)?0:1);
    }
}
