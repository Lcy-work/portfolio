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

/**
 * V1 - 计算每个时间点的平均组电压(V)和充电电流(A)
 * 运行方式: hadoop jar xxx.jar com.neu.datapro.V1_VoltageCurrent
 */
public class V1_VoltageCurrent {

    public static class VoltageCurrentMapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",");
            if (fields.length < 5) return;
            String packVoltage = fields[3], chargeCurrent = fields[4];
            if (packVoltage.equals("0.00") || chargeCurrent.equals("0.00")) return;
            String hour = (fields[1].length() >= 10) ? fields[1].substring(8, 10) : fields[1];
            context.write(new Text(hour), new Text(packVoltage + "," + chargeCurrent));
        }
    }

    public static class VoltageCurrentReducer extends Reducer<Text, Text, Text, Text> {
        private Connection conn;
        private PreparedStatement ps;

        public void setup(Context context) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/enger", "root", "root");
                conn.prepareStatement("DELETE FROM t_enger1").executeUpdate();
                ps = conn.prepareStatement("INSERT INTO t_enger1 (record_time,avg_pack_voltage,avg_charge_current) VALUES (?,?,?)");
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int count = 0; double totalV = 0, totalC = 0;
            for (Text v : values) {
                String[] f = v.toString().split(",");
                totalV += Double.parseDouble(f[0]); totalC += Double.parseDouble(f[1]); count++;
            }
            try { ps.setString(1, key.toString()); ps.setDouble(2, totalV/count); ps.setDouble(3, totalC/count); ps.executeUpdate(); }
            catch (SQLException e) { throw new RuntimeException(e); }
            context.write(key, new Text((totalV/count) + "," + (totalC/count)));
        }

        public void cleanup(Context context) {
            try { if (ps != null) ps.close(); if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    public static void main(String[] args) throws Exception {
        String namenode = args.length > 0 ? args[0] : "localhost";
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + namenode + ":9000");

        Job job = Job.getInstance(conf, "V1_VoltageCurrent");
        job.setJarByClass(V1_VoltageCurrent.class);
        job.setMapperClass(VoltageCurrentMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setReducerClass(VoltageCurrentReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        String in = args.length > 1 ? args[1] : "/Car/dsv13r1.csv";
        String out = args.length > 2 ? args[2] : "/Car/v1_out";
        FileInputFormat.addInputPath(job, new Path(in));
        FileOutputFormat.setOutputPath(job, new Path(out));
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(new Path(out))) fs.delete(new Path(out), true);

        System.out.println("V1 running...");
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
