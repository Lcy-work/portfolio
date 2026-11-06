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

/** V7 - 性能指标分类报告 */
public class V7_PerformanceIndex {
    public static class M extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable k, Text v, Context c) throws IOException, InterruptedException {
            String[] f = v.toString().split(",");
            if (f.length < 12 || f[10]==null || f[10].isEmpty()) return;
            c.write(new Text(f[10]), new Text(f[1]+","+f[7]+","+f[2]+",1"));
        }
    }
    public static class R extends Reducer<Text, Text, Text, Text> {
        private Connection conn; private PreparedStatement ps;
        public void setup(Context c) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/enger","root","root");
                conn.prepareStatement("DELETE FROM t_enger7").executeUpdate();
                ps = conn.prepareStatement("INSERT INTO t_enger7 (station_id,total_kwh,avg_charge_time,total_fees,session_count) VALUES(?,?,?,?,?)");
            } catch (SQLException e) { throw new RuntimeException(e); }
        }
        public void reduce(Text k, Iterable<Text> vs, Context c) throws IOException, InterruptedException {
            double tk=0, tt=0, tf=0; int n=0;
            for (Text v:vs) {
                String[] p = v.toString().split(",");
                if (p.length<4) continue;
                try { tk+=Double.parseDouble(p[0]); tt+=Double.parseDouble(p[1]); tf+=Double.parseDouble(p[2]); n++; }
                catch (NumberFormatException e) {}
            }
            try { ps.setString(1,k.toString()); ps.setDouble(2,tk); ps.setDouble(3,n>0?tt/n:0); ps.setDouble(4,tf); ps.setInt(5,n); ps.executeUpdate(); }
            catch (SQLException e) { throw new RuntimeException(e); }
            c.write(k, new Text(tk+","+(n>0?tt/n:0)+","+tf+","+n));
        }
        public void cleanup(Context c) { try { if(ps!=null)ps.close(); if(conn!=null)conn.close(); } catch (Exception e) {} }
    }
    public static void main(String[] a) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + (a.length>0?a[0]:"localhost") + ":9000");
        Job j = Job.getInstance(conf,"V7"); j.setJarByClass(V7_PerformanceIndex.class);
        j.setMapperClass(M.class); j.setMapOutputKeyClass(Text.class); j.setMapOutputValueClass(Text.class);
        j.setReducerClass(R.class); j.setOutputKeyClass(Text.class); j.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(j, new Path(a.length>1?a[1]:"/Car/nvv2t.csv"));
        FileOutputFormat.setOutputPath(j, new Path(a.length>2?a[2]:"/Car/v7_out"));
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(new Path(a.length>2?a[2]:"/Car/v7_out"))) fs.delete(new Path(a.length>2?a[2]:"/Car/v7_out"), true);
        System.out.println("V7 running...");
        System.exit(j.waitForCompletion(true)?0:1);
    }
}
