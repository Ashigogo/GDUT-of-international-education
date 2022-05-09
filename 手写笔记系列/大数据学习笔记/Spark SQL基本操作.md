## 大数据学习笔记03

### Spark SQL基本操作
1. 将下列json数据复制到你的ubuntu系统/usr/local/spark下，并保存命名为employee.json
```
{ "id":1 ,"name":" Ella","age":36 }
{ "id":2,"name":"Bob","age":29 }
{ "id":3 ,"name":"Jack","age":29 }
{ "id":4 ,"name":"Jim","age":28 }
{ "id":5 ,"name":"Damon" }
{ "id":5 ,"name":"Damon" }
```
- 首先为employee.json创建DataFrame，并写出Python语句完成以下操作：
- 创建DataFrame
```python
from pyspark import SparkContext,Sparkconf
form pyspark.sql import Sparksession
spark=SparkSession.builder().getOrCreate()
```
- 查询DataFrame的所有数据
```python
df = spark.read.json("file:///usr/local/spark/employee.json")
df.show()
```
- 查询所有数据，并去除重复的数据
```python
df.distinct().show()
```
- 查询所有数据，打印时去除id字段
```python
df.drop("id").show()
```
- 筛选age>30的记录
```python
df.filter(df.age > 30 ).show()
```
- 将数据按name分组
```python
df.groupBy("name").count().show()
```
- 将数据按name升序排列
```python
df.sort(df.name.asc()).show()
```
- 取出前3行数据
```python
df.take(3) 或python> df.head(3)
```
- 查询所有记录的name列，并为其取别名为username
```python
df.select(df.name.alias("username")).show()
```
- 查询年龄age的平均值
```python
df.agg({"age": "mean"}).show()
```
- 查询年龄age的最大值
```python
df.agg({"age": "max"}).show()
```
