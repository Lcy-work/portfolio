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
import java.util.*;

/** V4 - 充电频率分析 */
public class V4_ChargeFrequency {
    public static class M extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable k, Text v, Context c) throws IOException, InterruptedException {
            String[] f = v.toString().split(",");
            if (f.length < 6) return;
            try { Integer.parseInt(f[5]); } catch (NumberFormatException e) { return; }
            c.write(new Text(f[5]), new Text("1"));
        }
    }
    public static class R extends Reducer<Text, Text, Text, Text> {
        private Connection conn; private PreparedStatement ps;
        private Map<String,Integer> map = new HashMap<>(); private long total = 0;
        public void setup(Context c) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/enger","root","root");
                conn.prepareStatement("DELETE FROM t_enger4").executeUpdate();
                ps = conn.prepareStatement("INSERT INTO t_enger4 (charge_hour,charge_count,frequency_ratio) VALUES(?,?,?)");
            } catch (SQLException e) { throw new RuntimeException(e); }
        }
        public void reduce(Text k, Iterable<Text> vs, Context c) throws IOException, InterruptedException {
            int n=0; for (Text t:vs) n++; map.put(k.toString(), n);
            c.write(k, new Text(String.valueOf(n)));
        }
        public void cleanup(Context c) {
            try {
                for (int x : map.values()) total += x;
                for (Map.Entry<String,Integer> e : map.entrySet()) {
                    ps.setString(1,e.getKey()); ps.setInt(2,e.getValue());
                    ps.setDouble(3, total>0 ? (double)e.getValue()/total : 0);
                    ps.executeUpdate();
                }
                if(ps!=null)ps.close(); if(conn!=null)conn.close();
            } catch (SQLException ex) { throw new RuntimeException(ex); }
        }
    }
    public static void main(String[] a) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + (a.length>0?a[0]:"localhost") + ":9000");
        Job j = Job.getInstance(conf,"V4"); j.setJarByClass(V4_ChargeFrequency.class);
        j.setMapperClass(M.class); j.setMapOutputKeyClass(Text.class); j.setMapOutputValueClass(Text.class);
        j.setReducerClass(R.class); j.setOutputKeyClass(Text.class); j.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(j, new Path(a.length>1?a[1]:"/Car/nvv2t.csv"));
        FileOutputFormat.setOutputPath(j, new Path(a.length>2?a[2]:"/Car/v4_out"));
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(new Path(a.length>2?a[2]:"/Car/v4_out"))) fs.delete(new Path(a.length>2?a[2]:"/Car/v4_out"), true);
        System.out.println("V4 running...");
        System.exit(j.waitForCompletion(true)?0:1);
    }
}
