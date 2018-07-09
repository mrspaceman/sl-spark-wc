#!/bin/bash

SPARK_HOME=/home/aaspellc/spark-2.3.1-bin-hadoop2.7

export SPARK_LOCAL_IP=127.0.0.1
export SPARK_MASTER_HOST=127.0.0.1
export SPARK_WORKER_MEMORY=1g
export SPARK_EXECUTOR_MEMORY=512m
export SPARK_WORKER_INSTANCES=2
export SPARK_WORKER_CORES=2
export SPARK_WORKER_DIR=/home/aaspellc/work/sparkdata
mkdir -p $SPARK_WORKER_DIR


mvn package
mvn exec:java -Dexec.mainClass="uk.co.scottlogic.WordCount"  -Dexec.args="/usr/lib/jvm/default-java/docs/copyright"


$SPARK_HOME/sbin/start-master.sh
$SPARK_HOME/sbin/start-slave.sh spark://pyspark-1.scottlogic.co.uk:7077



$SPARK_HOME/bin/spark-class uk.co.scottlogic.WordCount /usr/lib/jvm/default-java/docs/copyright

$SPARK_HOME/bin/spark-submit \
  --class uk.co.scottlogic.WordCount \
  --master spark://127.0.0.1:7077 \
  /home/aaspellc/src/sl-spark-wc/target/wc-spark-1.0-SNAPSHOT.jar \
  "/usr/lib/jvm/default-java/docs/copyright"



$SPARK_HOME/bin/spark-submit \
  --class uk.co.scottlogic.WordCount \
  --master local \
  /home/aaspellc/src/sl-spark-wc/target/wc-spark-1.0-SNAPSHOT.jar \
  "/usr/lib/jvm/default-java/docs/copyright"
