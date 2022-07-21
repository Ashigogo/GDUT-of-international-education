package net.togogo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapred.LocatedFileStatusFetcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/*
*
* 修改本地hosts文件 添加ip映射
192.168.132.10  master
192.168.132.11  slave01
192.168.132.12  slave02
* */

public class HdfsAPI {

    static Configuration configuration = new Configuration();

    static {
        //设置hdfs的连接地址
        configuration.set("fs.defaultFS","hdfs://192.168.132.10:9000");
        //设置访问的用户名
        System.setProperty("HADOOP_USER_NAME","hd");
    }

    //上传
    public void upload(){
        FileSystem fileSystem = null;
        try {
            //得到filesystem对象
            fileSystem = FileSystem.get(configuration);
            //本地文件路径
            Path src = new Path("C:/Users/11852/Desktop/test uploda api.txt");
            //hdfs的路径
            Path dst = new Path("/data");

            //将本地文件上传到hdfs
            fileSystem.copyFromLocalFile(src,dst);
            System.out.println("上传成功");
        } catch (IOException e) {
            System.out.println("上传失败");
            e.printStackTrace();
        }finally {
            try {
                fileSystem.close();
        } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //下载
    public void download(){
        FileSystem fileSystem = null;
        FSDataInputStream open = null;
        FileOutputStream fileOutputStream = null;

        try {
            fileSystem = FileSystem.get(configuration);
            //根据hdfs的路径  获取            FSDataInputStream
            open = fileSystem.open(new Path("/data/test uploda api.txt"));
            //写出到本地
            fileOutputStream = new FileOutputStream("C:/Users/11852/Desktop/腾科实习记录/aa.html");
            //进行下载
            IOUtils.copyBytes(open,fileOutputStream,configuration);
            System.out.println("文件下载成功");
        } catch (IOException e) {
            System.out.println("文件下载失败");
            e.printStackTrace();
        }finally {
            //先进后出,后进先出关闭流
            try {
                fileOutputStream.close();
                open.close();
                fileSystem.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //遍历
    public void fileList(){

        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(configuration);
            RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(new Path("/"), true);
            while (iterator.hasNext()) {
                LocatedFileStatus fileStatus = iterator.next();
                //获取文件的信息
                long blockSize = fileStatus.getBlockSize();
                System.out.println("块信息大小:--->" + blockSize);
                //文件的名称
                String name = fileStatus.getPath().getName();
                System.out.println("文件名称:--->" + name);
            }
            //得到目录的信息
            FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));
            //进行遍历
            for(FileStatus fs:fileStatuses){
                if(!fs.isFile()){//得到的不是文件
                    System.out.println("目录信息:---->"+fs.getPath().getName());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileSystem.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        HdfsAPI hdfsAPI = new HdfsAPI();
        //hdfsAPI.upload();
        //hdfsAPI.download();
        hdfsAPI.fileList();
    }
}
