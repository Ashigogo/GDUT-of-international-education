#### [1873. 计算特殊奖金](https://leetcode.cn/problems/calculate-special-bonus/)

表: Employees

| 列名        | 类型    |
| ----------- | ------- |
| employee_id | int     |
| name        | varchar |
| salary      | int     |

employee_id 是这个表的主键。
此表的每一行给出了雇员id ，名字和薪水。

写出一个SQL 查询语句，计算每个雇员的奖金。如果一个雇员的id是奇数并且他的名字不是以'M'开头，那么他的奖金是他工资的100%，否则奖金为0。

Return the result table ordered by employee_id.

```sql
题解:使用case when流程控制语句
select employee_id, 
case when (employee_id %2 = 1 and name not like 'M%') then salary else 0 end as bonus
from employees
order by employee_id;
```



#### [627. 变更性别](https://leetcode.cn/problems/swap-salary/)

Salary 表：

| column | type    |
| ------ | ------- |
| id     | int     |
| name   | varchar |
| sex    | ENUM    |
| salary | int     |

id 是这个表的主键。

sex 这一列的值是 ENUM 类型，只能从 ('m', 'f') 中取。

本表包含公司雇员的信息。

请你编写一个 SQL 查询来交换所有的 'f' 和 'm' （即，将所有 'f' 变为 'm' ，反之亦然），仅使用 单个 update 语句 ，且不产生中间临时表。

注意，你必须仅使用一条 update 语句，且 不能 使用 select 语句。

```sql
题解:使用if条件语句
update salary
set sex = if(sex='m','f','m')
```

#### [196. 删除重复的电子邮箱](https://leetcode.cn/problems/delete-duplicate-emails/)

表: Person

| column | type    |
| ------ | ------- |
| id     | int     |
| email  | varchar |


id是该表的主键列。
该表的每一行包含一封电子邮件。电子邮件将不包含大写字母。


编写一个 SQL 删除语句来 删除 所有重复的电子邮件，只保留一个id最小的唯一电子邮件。

以 任意顺序 返回结果表。 （注意： 仅需要写删除语句，将自动对剩余结果进行查询）

```sql
题解:找到最小id,其他的删除,创建为临时表
delete from person
where id 
not in
(select * from
    (select min(id)
     from person
     group by email
    )
    as p1
)
```

#### [511. 游戏玩法分析 I](https://leetcode.cn/problems/game-play-analysis-i/)

活动表 `Activity`：

| column name  | Type |
| ------------ | ---- |
| player_id    | int  |
| device_id    | int  |
| event_date   | date |
| games_played | int  |

表的主键是 (player_id, event_date)。
这张表展示了一些游戏玩家在游戏平台上的行为活动。
每行数据记录了一名玩家在退出平台之前，当天使用同一台设备登录平台后打开的游戏的数目（可能是 0 个）。

写一条 SQL 查询语句获取每位玩家 **第一次登陆平台的日期**。

```sql
select player_id,min(event_date) first_login
from activity
group by playred_id
```

#### [512. 游戏玩法分析 II](https://leetcode.cn/problems/game-play-analysis-ii/)

Table: `Activity`

| column name  | Type |
| ------------ | ---- |
| player_id    | int  |
| device_id    | int  |
| event_date   | date |
| games_played | int  |

(player_id, event_date) 是这个表的两个主键
这个表显示的是某些游戏玩家的游戏活动情况
每一行是在某天使用某个设备登出之前登录并玩多个游戏（可能为0）的玩家的记录

请编写一个 SQL 查询，描述每一个玩家首次登陆的设备名称

```sql
select player_id,device_id
from activity
where (player_id,event_date)
in (
    select player_id,min(event_date)
    from activity
    group by player_id
   )
```

