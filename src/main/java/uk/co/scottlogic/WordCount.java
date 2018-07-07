package uk.co.scottlogic;

import com.twitter.chill.Externalizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.elasticsearch.hadoop.mr.EsInputFormat;
import org.elasticsearch.hadoop.mr.EsOutputFormat;
import org.elasticsearch.hadoop.mr.WritableArrayWritable;
import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import java.util.Arrays;
import java.util.List;
import java.util.Arrays;
import java.util.List;

public class WordCount {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify a filename");
        } else {
            try {
                new WordCount().runWordCount(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void runProducer(final String filename) throws Exception {
        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();
        index = 0;

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            Consumer<String> consumerNames = line -> {
                try {
                    RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC, index++, line.trim())).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            stream.forEach(consumerNames);

        } finally {
            producer.close();
        }
    }

    public void runWordCount(String filename)
    {
        JavaRDD<String> textFile = sc.textFile("hdfs://...");
        JavaPairRDD<String, Integer> counts = textFile
                .flatMap(s -> Arrays.asList(s.split("\\s+")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b);
        counts.saveAsTextFile("hdfs://...");
    }
}
