create database wordcount;
show databases;

use wordcount;
create table file_data(context string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\n';
load data local inpath '/home/hd/wordcount.txt' into table file_data;
select * from file_data;

create table words(context string);
insert into table words select explode(split(context ," "))from file_data;

select context, count(context) from words group by context;
