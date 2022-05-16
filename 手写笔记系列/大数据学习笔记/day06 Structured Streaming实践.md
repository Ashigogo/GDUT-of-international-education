##  大数据学习笔记06
### Structured Streaming实践

**Structured Streaming基本操作**
1. 编写Structured Streaming程序的基本步骤
- 实例任务：一个包含很多行英文语句的数据流源源不断到达，Structured Streaming
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
```
```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# 导入需要用到的模块
import os
import shutil
import random
import time


TEST_DATA_TEMP_DIR = '/tmp/'
TEST_DATA_DIR = '/tmp/testdata/'

ACTION_DEF = ['login', 'logout', 'purchase']
DISTRICT_DEF = ['fujian', 'beijing', 'shanghai', 'guangzhou']
JSON_LINE_PATTERN = '{{"eventTime": {}, "action": "{}", "district": "{}"}}\n'


# 测试的环境搭建，判断文件夹是否存在，如果存在则删除旧数据，并建立文件夹
def test_setUp():
    if os.path.exists(TEST_DATA_DIR):
        shutil.rmtree(TEST_DATA_DIR, ignore_errors=True)
    os.mkdir(TEST_DATA_DIR)


# 测试环境的恢复，对文件夹进行清理
def test_tearDown():
    if os.path.exists(TEST_DATA_DIR):
        shutil.rmtree(TEST_DATA_DIR, ignore_errors=True)


# 生成测试文件
def write_and_move(filename, data):
    with open(TEST_DATA_TEMP_DIR + filename,
              "wt", encoding="utf-8") as f:
        f.write(data)

    shutil.move(TEST_DATA_TEMP_DIR + filename,
                TEST_DATA_DIR + filename)


if __name__ == "__main__":
    test_setUp()

    for i in range(1000):
        filename = 'e-mall-{}.json'.format(i)

        content = ''
        rndcount = list(range(100))
        random.shuffle(rndcount)
        for _ in rndcount:
            content += JSON_LINE_PATTERN.format(
                str(int(time.time())),
                random.choice(ACTION_DEF),
                random.choice(DISTRICT_DEF))
        write_and_move(filename, content)

        time.sleep(1)

    test_tearDown()

```
```shell
$ vim spark_ss_filesource.py
```
```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# 导入需要用到的模块
import os
import shutil
from pprint import pprint

from pyspark.sql import SparkSession
from pyspark.sql.functions import window, asc
from pyspark.sql.types import StructType, StructField
from pyspark.sql.types import TimestampType, StringType


# 定义JSON文件的路径常量
TEST_DATA_DIR_SPARK = 'file:///tmp/testdata/'


if __name__ == "__main__":
    # 定义模式，为时间戳类型的eventTime、字符串类型的操作和省份组成
    schema = StructType([
        StructField("eventTime", TimestampType(), True),
        StructField("action", StringType(), True),
        StructField("district", StringType(), True)])

    spark = SparkSession \
        .builder \
        .appName("StructuredEMallPurchaseCount") \
        .getOrCreate()

    spark.sparkContext.setLogLevel('WARN')

    lines = spark \
        .readStream \
        .format("json") \
        .schema(schema) \
        .option("maxFilesPerTrigger", 100) \
        .load(TEST_DATA_DIR_SPARK)

    # 定义窗口
    windowDuration = '1 minutes'

    windowedCounts = lines \
        .filter("action = 'purchase'") \
        .groupBy('district', window('eventTime', windowDuration)) \
        .count() \
        .sort(asc('window'))

    query = windowedCounts \
        .writeStream \
        .outputMode("complete") \
        .format("console") \
        .option('truncate', 'false') \
        .trigger(processingTime="10 seconds") \
        .start()

    query.awaitTermination()
```
执行后便可在流处理终端查看到实时的结果。
