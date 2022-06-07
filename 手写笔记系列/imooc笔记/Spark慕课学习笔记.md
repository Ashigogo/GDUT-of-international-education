## Spark慕课学习笔记
[TOC]



### RDD特性

RDD[官网源码]( [spark/RDD.scala at master · apache/spark (github.com)](https://github.com/apache/spark/blob/master/core/src/main/scala/org/apache/spark/rdd/RDD.scala) ):ear:

RDD 是什么？Resilient Distributed Dataset (RDD) 弹性分布式数据集，Spark中的基本抽象。表示不可变的，可以并行操作的元素的分区集合。

 RDD具有五个主要特性： 
- 有一系列的分区/分片（partition/split）

- 用于计算每次分割的函数

  y = f(x)

  rdd.map( _+1)

- 其他 RDD 的依赖关系列表

  rdd1==>rdd2==>rdd3==>rdd4

  rdda = 5个partition

  ==>map

  rddb = 5个partition

  注：如果rdda里面某个分区丢失，由于依赖关系，rddb只对丢失的分区重新计算

- 键值 RDD 的分区程序（例如，说 RDD 是哈希分区的）

- 用于计算每个拆分的首选位置列表（例如，块位置 一个 HDFS 文件）

  数据在哪，优先把作业调度到数据所在的节点进行计算:移动数据不如移动计算:clown_face:
### SparkContext&SparkConf
首先创建SparkContext（主入口）
- 连接Spark“集群”：local,standalone,yarn,mesos
在创建SparkContext之前还需要创建一个SparkConf对象（一些配置用于设置参数）


