# spark_ml
spark ml 实战(scala)
***
* 机器学习视频:<a href="http://edu.51cto.com/course/course_id-3560.html" target="_blank">机器学习</a>
* 推荐系统视频:<a href="http://edu.51cto.com/lecturer/user_id-7516873.html" target="_blank">推荐系统</a>


***
本地安装 spark
version:
***
**修改配置文件crawler-defaults.conf**
``` python
1.下载spark
spark-2.2.0-bin-hadoop2.7.tgz [https://d3kbcqa49mib13.cloudfront.net/spark-2.2.0-bin-hadoop2.7.tgz]
2.下载winutils  【https://github.com/steveloughran/winutils/blob/master/hadoop-2.7.1/bin/winutils.exe】
3.将winutils.exe复制到spark-2.2.0-bin-hadoop2.7\bin目录下。
4.设置环境变量HADOOP_HOME = D:\tools\hadoop\spark-2.2.0-bin-hadoop2.7根目录 
5.设置Path : ;D:\tools\hadoop\spark-2.2.0-bin-hadoop2.7\bin
6. winutils.exe chmod -R 777 D:\workspace 需管理员命令行运行
```