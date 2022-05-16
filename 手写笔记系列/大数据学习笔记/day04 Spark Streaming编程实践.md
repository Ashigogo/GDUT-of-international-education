## 大数据学习笔记04
### Spark Streaming编程实践
在pyspark中创建文件流
首先在系统中打开第一个终端(记作数据源终端),命令如下:
```shell
$ cd /usr/local/spark/mycode
$ mkdir streaming
$ cd streaming
$ mkdir logfile
$ cd logfile
```
其次在系统中打开第二个终端(记作流计算终端)
启动进入pyspark后,输入如下命令:
```python
>>> from pyspark import SparkContext
>>> from pyspark.streaming import StreamingContext
>>> ssc = StreamingContext(sc, 10)
>>> lines = ssc. \
... textFileStream('file:///usr/local/spark/mycode/streaming/logfile')
>>> words = lines.flatMap(lambda line: line.split(' '))
>>> wordCounts = words.map(lambda x : (x,1)).reduceByKey(lambda a,b:a+b)
>>> wordCounts.pprint()
>>> ssc.start()
>>> ssc.awaitTermination()
```
在pyspark中输入ssc.start()后,程序就进入了循环监听状态.
实际上，当你输入这行回车后，Spark Streaming就开始进行循环监听，下面的ssc.awaitTermination()是无法输入到屏幕上的，但是，为了程序完整性，这里还是给出ssc.awaitTermination()
```
所以，上面在pyspark中执行的程序，一旦你输入ssc.start()以后，程序就开始自动进入循环监听状态，屏幕上会显示一堆的信息.
```
这里我们新建了文件
```shell
$ vim /usr/local/spark/mycode/streaming/logfile/log2.txt
```
任意输入一些数据即可,例如:
```
hadoop is good!
spark is fast.
```
这样就可以在显示终端看到实时输入的数据了.

