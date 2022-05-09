## 大数据学习笔记02

### Pyspark交互式编程
1. 有该数据集Data01.txt 该数据集包含了某大学计算机系的成绩，数据格式如下所示：
```html
Tom,DataBase,80
Tom,Algorithm,50
Jim,DataBase,90
Jim,Algorithm,60......
```
根据给定的数据集，在pyspark中通过编程来完成以下内容：
- 该系总共有多少学生；  (提前启动好pyspark)
```shell
lines = sc.textFile("file:///usr/local/spark/sparksqldata/Data01.txt")加载文件
res = lines.map(lambda x:x.split(",")).map(lambda x: x[0]) //获取每行数据的第1列 
distinct_res = res.distinct()  //去重操作
distinct_res.count()//取元素总个数
```
- 该系共开设了多少门课程；
```shell
lines = sc.textFile("file:///usr/local/spark/sparksqldata/Data01.txt")
res = lines.map(lambda x:x.split(",")).map(lambda x:x[1]) //获取每行数据的第2列
distinct_res = res.distinct()//去重操作
distinct_res.count()//取元素总个数
```
- Tom同学的总成绩平均分是多少；
```shell
lines = sc.textFile("file:///usr/local/spark/sparksqldata/Data01.txt")
res = lines.map(lambda x:x.split(",")).filter(lambda x:x[0]=="Tom") //筛选Tom同学的成绩信息
res.foreach(print) 
score = res.map(lambda x:int(x[2])) //提取Tom同学的每门成绩，并转换为int类型
num = res.count() //Tom同学选课门数
sum_score = score.reduce(lambda x,y:x+y) //Tom同学的总成绩
avg = sum_score/num // 总成绩/门数=平均分
print(avg)
```
- 求每名同学的选修的课程门数；
```shell 
lines = sc.textFile("file:///usr/local/spark/sparksqldata/Data01.txt")
res = lines.map(lambda x:x.split(",")).map(lambda x:(x[0],1)) //学生每门课程都对应(学生姓名,1)，学生有n门课程则有n个(学生姓名,1)
each_res = res.reduceByKey(lambda x,y: x+y) //按学生姓名获取每个学生的选课总数
each_res.foreach(print)
```
- 该系DataBase课程共有多少人选修；
```shell
lines = sc.textFile("file:///usr/local/spark/sparksqldata/Data01.txt")
res = lines.map(lambda x:x.split(",")).filter(lambda x:x[1]=="DataBase")
res.count()
```
- 各门课程的平均分是多少；
```shell
lines = sc.textFile("file:///usr/local/spark/sparksqldata/Data01.txt")
res = lines.map(lambda x:x.split(",")).map(lambda x:(x[1],(int(x[2]),1))) //为每门课程的分数后面新增一列1，表示1个学生选择了该课程。格式如('ComputerNetwork', (44, 1))
temp = res.reduceByKey(lambda x,y:(x[0]+y[0],x[1]+y[1])) //按课程名聚合课程总分和选课人数。格式如('ComputerNetwork', (7370, 142))
avg = temp.map(lambda x:(x[0], round(x[1][0]/x[1][1],2)))//课程总分/选课人数 = 平均分，并利用round(x,2)保留两位小数
avg.foreach(print)
```
- 使用累加器计算共有多少人选了DataBase这门课。
```shell
lines = sc.textFile("file:///usr/local/spark/sparksqldata/Data01.txt")
res = lines.map(lambda x:x.split(",")).filter(lambda x:x[1]=="DataBase")//筛选出选了DataBase课程的数据
accum = sc.accumulator(0) //定义一个从0开始的累加器accum
res.foreach(lambda x:accum.add(1))//遍历res，每扫描一条数据，累加器加1
accum.value //输出累加器的最终值
```
#### 编写独立应用程序实现数据去重

- 对于两个输入文件A和B，编写Spark独立应用程序，对两个文件进行合并，并剔除其中重复的内容，得到一个新文件C。
- 假设当前目录为/usr/local/spark/mycode/remdup，该目录下新建一个remdup.py文件
```python
from pyspark import SparkContext#初始化SparkContext
sc = SparkContext('local','remdup')#加载两个文件A和B
lines1 = sc.textFile("file:///usr/local/spark/mycode/remdup/A")
lines2 = sc.textFile("file:///usr/local/spark/mycode/remdup/B")#合并两个文件的内容
lines = lines1.union(lines2)#去重操作
distinct_lines = lines.distinct() #排序操作
res = distinct_lines.sortBy(lambda x:x)
#将结果写入result文件中，repartition(1)的作用是让结果合并到一个文件中，不加的话会结果写入到两个文件
res.repartition(1).saveAsTextFile("file:///usr/local/spark/mycode/remdup/result")
```
```python
#最后在目录/usr/local/spark/mycode/remdup下执行下面命令执行程序（注意执行程序时请先退出pyspark shell，否则会出现“地址已在使用”的警告）；
```
#### 编写独立应用程序实现求平均值问题
- 每个输入文件表示班级学生某个学科的成绩，每行内容由两个字段组成，第一个是学生名字，第二个是学生的成绩；编写Spark独立应用程序求出所有学生的平均成绩，并输出到一个新文件中。
- 假设当前目录为/usr/local/spark/mycode/avgscore，在当前目录下新建一个avgscore.py,执行如下代码
```python
from pyspark import SparkContext
sc = SparkContext('local',' avgscore')
lines1 = sc.textFile("file:///usr/local/spark/mycode/avgscore/Algorithm.txt")
lines2 = sc.textFile("file:///usr/local/spark/mycode/avgscore/Database.txt")
lines3 = sc.textFile("file:///usr/local/spark/mycode/avgscore/Python.txt")
lines = lines1.union(lines2).union(lines3)
data = lines.map(lambda x:x.split(" ")).map(lambda x:(x[0],(int(x[1]),1)))
res = data.reduceByKey(lambda x,y:(x[0]+y[0],x[1]+y[1]))
result = res.map(lambda x:(x[0],round(x[1][0]/x[1][1],2)))
result.repartition(1).saveAsTextFile("file:///usr/local/spark/mycode/avgscore/result")
```
