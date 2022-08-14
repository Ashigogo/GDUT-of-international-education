import pymysql
from flask import Flask,url_for,render_template,json
app = Flask(__name__)

#连接数据库获取数据
def getData():
    #访问集群上的MySQL获取connect
    db = pymysql.connect(user="root",password="root",host="192.168.132.10",database="sogou",charset="utf8");
    #获取游标
    cursor = db.cursor();
    #执行SQL
    cursor.execute("select * from sogou_top50");
    #得到数据
    alldata = cursor.fetchall();
    return  alldata;

@app.route("/top50Json")
def top50Json():
    #获取查询到的数据,转换成JSON格式,发送到页面
    allData = getData();
    #转换成json格式
    t1 = [];
    t2 = [];
    for res in allData:
        t1.append(res[0]);
        t2.append(res[1]);
        jsonvalue = json.dumps({"keyword":t1,"count":t2});
        print(jsonvalue);
    return jsonvalue;

@app.route("/top50")
def top50():
    return render_template("top50.html");


if __name__=='__main__':
    app.run();