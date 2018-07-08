package uk.co.scottlogic;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

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

    void runWordCount(final String filename) throws Exception {
        SparkConf conf = new SparkConf().setAppName("wordcount").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile(filename);

        JavaPairRDD<String, Integer> counts = lines
                .flatMap(s -> Arrays.asList(s.split("\\s+")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b);
        counts.saveAsTextFile(filename + ".wordcounts");
    }


}
