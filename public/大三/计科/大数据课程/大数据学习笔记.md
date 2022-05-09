## 大数据学习笔记

学习林子雨大数据教材的一些笔记

### HDFS常用操作
1. 启动Hadoop，在HDFS中创建用户目录“/user/hadoop”；   
   ```shell
   $ cd /usr/local/hadoop
   $ ./sbin/start-dfs.sh
   $ jps
   ```
2. 在Linux系统的本地文件系统的“/usr/local/spark/mycode”目录下新建一个文本文件Word.txt
   ```shell
   $ cd /usr/local/spark
   $ mkdir mycode
   $ touch Word.txt   ||  $vim Word.txt
   ```
3. 将HDFS中Word.txt文件在终端显示
   ```shell
   $ pwd   查看所在路径     
   $ ./bin/hdfs dfs -cat ./Word.txt
   ```
4. 在HDFS中的“/user/hadoop”目录下，创建子目录input，把HDFS中“/user/hadoop”目录下的test.txt文件，复制到“/user/hadoop/input”目录下；
   ```shell
   $ cd  /usr/local/hadoop
   $ ./bin/hdfs  dfs  -mkdir  /user/hadoop/input
   $ ./bin/hdfs  dfs  -cp  /user/hadoop/test.txt  /user/hadoop/input
   ```
5. 删除HDFS中“/user/hadoop”目录下的test.txt文件，删除HDFS中“/user/hadoop”目录下的input子目录及其子目录下的所有内容。
   ```shell
   $ cd  /usr/local/hadoop
   $ ./bin/hdfs  dfs  -rm  /user/hadoop/test.txt
   $ ./bin/hdfs  dfs  -rm  -r  /user/hadoop/input
   ```

### Spark读取文件系统的数据
1. 在pyspark中读取Linux系统本地文件“/home/hadoop/Word.txt”,Spark安装
   在“/usr/local/spark”目录。
   ```shell
   $ cd  /usr/local/spark
   $./bin/pyspark
   >>> textFile=sc.textFile("file:///home/hadoop/test.txt")
   >>> linecount=textFile.count()
   >>> print(linecount)
   ```
2. 在pyspark中读取HDFS系统文件“/user/hadoop/test.txt”（如果该文件不存在，请先创建），然后，统计出文件的行数；
   ```shell
   >>> textFile=sc.textFile("hdfs://localhost:9000/user/hadoop/test.txt")
   >>> linecount=textFile.count()
   >>> print(linecount)
   ```
3. 编写独立应用程序，读取HDFS系统文件“/user/hadoop/test.txt”
   ```shell
   $  cd  ~
   $  vim test.txt   #自行输入一些文本数据,然后保存退出(Esc :wq)
   $  cd  /usr/local/hadoop  #进入Hadoop的安装目录
   $  ./sbin/start-dfs.sh   #启动Hadoop
   $  ./bin/hdfs dfs -put ~/test.txt /user/hadoop  #把本地文件test.txt上传到HDFS中
   $  ./bin/hdfs dfs -ls /user/hadoop/  #验证文件是否上传到HDFS中
   $  ./bin/hdfs dfs -cat /user/hadoop/test.txt  #查看HDFS中的test.txt文件内容
   $ cd  ~           # 进入用户主文件夹
   $ mkdir  ./sparkapp        # 创建应用程序根目录
   $ cd sparkapp
   $ vim SimpleApp.py         #编写该应用程序
   $ cd ~/sparkapp
   $ /usr/local/spark/bin/spark-submit SimpleApp.py   #submit方式运行该程序,查看到结果
   ```
