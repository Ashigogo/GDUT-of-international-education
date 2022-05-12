## 大数据学习笔记05
### 初识Kafka及实践
Kafka是一种高吞吐量的分布式订阅消息系统,这里我们将Kafka作为数据源,让Kafka产生数据发送给Spark Streaming应用程序,Spark Streaming应用程序再对接收到的数据进行实时处理,从而完成一个典型的流计算过程。
实验步骤如下:
- 第一个终端启动zookeeper
```shell
./bin/zookeeper-server-start.sh config/zookeeper.properties
```
- 第二个终端启动kafka
```shell
bin/kafka-server-start.sh config/server.properties
```
- 第三个终端测试kafka是否启动正常
```shell
./bin/kafka-topics.sh  --create  --zookeeper  localhost:2181  \
>--replication-factor  1  --partitions  1  --topic  wordsendertest
```
可以用list列出所有创建的Topic，验证是否创建成功
```shell
./bin/kafka-topics.sh  --list  --zookeeper  localhost:2181
```
- 下面用生产者（Producer）来产生一些数据，请在当前终端（记作“数据源终端”）内继续输入下面命令：
```shell
 ./bin/kafka-console-producer.sh  --broker-list  localhost:9092 \
>  --topic  wordsendertest
```
- 第四个终端启动消费者
```shell
./bin/kafka-console-consumer.sh  --zookeeper  localhost:2181  \
> --topic  wordsendertest  --from-beginning
```
在生产者终端即数据输入端任意输入一些数据,即可在消费者终端即显示终端看到实时输入的数据了.