##  大数据学习笔记06
### Structured Streaming实践

**Structured Streaming基本操作**
**1. 编写Structured Streaming程序的基本步骤**
实例任务：一个包含很多行英文语句的数据流源源不断到达，Structured Streaming
程序对每行英文语句进行拆分，并统计每个单词出现的频率。

- 启动HDFS,命令如下:
```shell
$ cd /usr/local/hadoop
$ sbin/start-dfs.sh
```
- 在/usr/local/spark/mycodes/structuredstreaming路径下编写StructuredNetworkWordCount.py程序
```shell
$ cd /usr/local/spark/mycodes/structuredstreaming
$ vim StructuredNewworkWordCount.py
```
```python
#!/usr/bin/env python3

from pyspark.sql import SparkSession
from pyspark.sql.functions import split
from pyspark.sql.functions import explode


if __name__ == "__main__":
    spark = SparkSession \
        .builder \
        .appName("StructuredNetworkWordCount") \
        .getOrCreate()

    spark.sparkContext.setLogLevel('WARN')

    lines = spark \
        .readStream \
        .format("socket") \
        .option("host", "localhost") \
        .option("port", 9999) \
        .load()

    words = lines.select(
        explode(
            split(lines.value, " ")
        ).alias("word")
    )

    wordCounts = words.groupBy("word").count()

    query = wordCounts \
        .writeStream \
        .outputMode("complete") \
        .format("console") \
        .trigger(processingTime="8 seconds") \
        .start()

    query.awaitTermination()
```
- 新建一个数据源终端,命令如下:
```shell
$ nc -lk 9999
```
- 再新建一个流计算终端,命令如下:
```shell
$ cd /usr/local/spark/mycode/structuredstreaming/
$ /usr/local/spark/bin/spark-submit StructuredNetworkWordCount.py
```
- 此时在数据源中任意输入英文流,切换到流计算终端即可查看到实时的显示的结果。

**2. File源（或称为“文件源”）以文件流的形式读取某个目录中的文件，支持的文件格式为csv、json、orc、parquet、text等。**
- 命令如下:
```shell
$ cd /usr/local/spark/mycode/structuredstreaming/file
```
- 在该文件夹下创建两个文件spark_ss_filesource_generate.py和spark_ss_filesource.py命令如下:
```shell
$ vim spark_ss_filesource_generate.py
$ vim spark_ss_filesource.py
代码过长,此处就不展示了。
```
执行后便可在流处理终端查看到实时的结果。
