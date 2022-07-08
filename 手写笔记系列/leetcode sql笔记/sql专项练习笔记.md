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
DELETE FROM PERSON
WHERE ID 
NOT IN
(SELECT * FROM
  ( SELECT MIN(ID)
    FROM PERSON
    GROUP BY EMAIL
  ) 
AS P1)
```

