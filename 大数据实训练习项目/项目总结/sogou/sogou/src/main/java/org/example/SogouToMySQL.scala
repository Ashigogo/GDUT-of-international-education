package org.example

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.example.Sogou

import java.util.Properties

/*
*
* ./spark-submit --class  org.example.SogouToMySQL
* --jars /home/hd/apps/spark/jars/mysql-connector-java-8.0.25.jar
* /home/hd/data/sogou-1.0-SNAPSHOT.jar
*
* */


object SogouToMySQL {

  def main(args: Array[String]): Unit = {
    //得到sparkSession
    val sparkSession = SparkSession.builder()
      .appName("CaseClassDemo")
      .master("local[2]")
      .getOrCreate();
    //通过sparksession得到sparkcontext
    val sc = sparkSession.sparkContext;

    //读取文件 x=>x等同于 _
    val rdd = sc.textFile("/data/sogou.500w.utf8");
    //val data = rdd.map(x=>(x.split(",")));
    val data = rdd.map(_.split("\t"));

    //进行关联
    val sogouRDD = data.map(x=>Sogou(x(0).toString,x(1).toString,x(2).toString,x(3).toInt,x(4).toInt,x(5).toString));

    //将rdd转换成pf
    //导入隐式转换
    import sparkSession.implicits._
    val sogouDF= sogouRDD.toDF
    //将DF转换成表
    sogouDF.createOrReplaceTempView("t_sogou");
    //进行查询
    val res = sparkSession.sql("SELECT keyword , count(keyword) as countkeyword from t_sogou   where  (keyword LIKE '%百度%' or  keyword LIKE '%小米%' or  keyword LIKE '%仙剑奇侠传%' ) GROUP by keyword;");
    //手动创建数据库  create database sogou character set utf8;
    val url="jdbc:mysql://192.168.132.10:3306/sogou?useUnicode=true&characterEncoding=utf-8&relaxAutoCommit=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true";
    val table ="sogou_top50";
    val property = new Properties();
    property.put("user","root");
    property.put("password","root");
    property.put("driver","com.mysql.cj.jdbc.Driver")
    //将结果保存到数据库
    res.write.mode(SaveMode.Append).jdbc(url,table,property)
    sc.stop();
    sparkSession.stop();


  }
}
