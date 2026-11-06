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

/** V2 - 最高/最低单体电压 */
public class V2_CellVoltage {
    public static class M extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable k, Text v, Context c) throws IOException, InterruptedException {
            String[] f = v.toString().split(",");
            if (f.length < 7) return;
            if (!f[5].equals("0.00")) c.write(new Text("maxcv"), new Text(f[5]));
            if (!f[6].equals("0.00")) c.write(new Text("mincv"), new Text(f[6]));
        }
    }
    public static class R extends Reducer<Text, Text, Text, Text> {
        private Connection conn; private PreparedStatement ps;
        public void setup(Context c) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/enger","root","root");
                conn.prepareStatement("DELETE FROM t_enger2").executeUpdate();
                ps = conn.prepareStatement("INSERT INTO t_enger2 (voltage_type,voltage_value) VALUES(?,?)");
            } catch (SQLException e) { throw new RuntimeException(e); }
        }
        public void reduce(Text k, Iterable<Text> vs, Context c) throws IOException, InterruptedException {
            boolean isMax = k.toString().equals("maxcv");
            double r = isMax ? Double.MIN_VALUE : Double.MAX_VALUE;
            for (Text t : vs) { double d = Double.parseDouble(t.toString()); r = isMax ? Math.max(r,d) : Math.min(r,d); }
            try { ps.setString(1, k.toString()); ps.setDouble(2, r); ps.executeUpdate(); }
            catch (SQLException e) { throw new RuntimeException(e); }
            c.write(k, new Text(String.valueOf(r)));
        }
        public void cleanup(Context c) { try { if(ps!=null)ps.close(); if(conn!=null)conn.close(); } catch (Exception e) {} }
    }
    public static void main(String[] a) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + (a.length>0?a[0]:"localhost") + ":9000");
        Job j = Job.getInstance(conf,"V2"); j.setJarByClass(V2_CellVoltage.class);
        j.setMapperClass(M.class); j.setMapOutputKeyClass(Text.class); j.setMapOutputValueClass(Text.class);
        j.setReducerClass(R.class); j.setOutputKeyClass(Text.class); j.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(j, new Path(a.length>1?a[1]:"/Car/dsv13r1.csv"));
        FileOutputFormat.setOutputPath(j, new Path(a.length>2?a[2]:"/Car/v2_out"));
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(new Path(a.length>2?a[2]:"/Car/v2_out"))) fs.delete(new Path(a.length>2?a[2]:"/Car/v2_out"), true);
        System.out.println("V2 running...");
        System.exit(j.waitForCompletion(true)?0:1);
    }
}
