## 慢查询日志
MySQL的慢查询日志是MySQL提供的一种日志记录，它用来记录在MySQL中响应时间超过阀值的语句，具体指运行时间超过long_query_time值的SQL，则会被记录到慢查询日志中。<br>
long_query_time的默认值为10，意思是运行10S以上的语句。默认情况下，Mysql数据库并不启动慢查询日志，需要我们手动来设置这个参数(set global slow_query_log=1;)。<br>
使用set global slow_query_log=1开启了慢查询日志只对当前数据库生效，如果MySQL重启后则会失效。如果要永久生效，就必须修改配置文件my.cnf（其它系统变量也是如此）<br>
当然，如果不是调优需要的话，一般不建议启动该参数，因为开启慢查询日志会或多或少带来一定的性能影响。<br>
慢查询日志支持将日志记录写入文件，也支持将日志记录写入数据库表。<br>

## 最左匹配原则
顾名思义：最左优先，以最左边的为起点任何连续的索引都能匹配上。同时遇到范围查询(>、<、between、like)就会停止匹配。<br>
例如：b = 2 如果建立(a,b)顺序的索引，是匹配不到(a,b)索引的；<br>
但是如果查询条件是a = 1 and b = 2或者a=1(又或者是b = 2 and b = 1)就可以，因为优化器会自动调整a,b的顺序;<br>
再比如a = 1 and b = 2 and c > 3 and d = 4 如果建立(a,b,c,d)顺序的索引，d是用不到索引的，因为c字段是一个范围查询，它之后的字段会停止匹配。<br>
- 原理分析
  我们都知道索引的底层是一颗B+树，那么联合索引当然还是一颗B+树，只不过联合索引的健值数量不是一个，而是多个。
  构建一颗B+树只能根据一个值来构建，因此数据库依据联合索引最左的字段来构建B+树。<br>
  例子：假如创建一个（a,b)的联合索引，那么它的索引树是这样的<br>
![图示](https://raw.githubusercontent.com/ShanShuan/myBlog/master/mysql/src/main/resources/uq.jpg)<br>
  可以看到a的值是有顺序的，1，1，2，2，3，3，而b的值是没有顺序的1，2，1，4，1，2。所以b = 2这种查询条件没有办法利用索引，因为联合索引首先是按a排序的，b是无序的。<br>
  同时我们还可以发现在a值相等的情况下，b值又是按顺序排列的，但是这种顺序是相对的。所以最左匹配原则遇上范围查询就会停止，剩下的字段都无法使用索引。例如a = 1 and b = 2 a,b字段都可以使用索引，因为在a值确定的情况下b是相对有序的，而a>1and b=2，a字段可以匹配上索引，但b值不可以，因为a的值是一个范围，在这个范围中b是无序的。<br>
 
## ExpLain
expain出来的信息有10列，分别是id、select_type、table、type、possible_keys、key、key_len、ref、rows、Extra<br>
概要描述：<br>
- id:选择标识符<br>
SELECT识别符。这是SELECT的查询序列号
1. id相同时，执行顺序由上至下<br>
2. 如果是子查询，id的序号会递增，**_`id值越大优先级越高，越先被执行`_**<br>
3. id如果相同，可以认为是一组，从上往下顺序执行；在所有组中，id值越大，优先级越高，越先执行<br>
   
- select_type:表示查询的类型<br>
(1) SIMPLE(简单SELECT，不使用UNION或子查询等)<br>
(2) PRIMARY(子查询中最外层查询，查询中若包含任何复杂的子部分，最外层的select被标记为PRIMARY)<br>
(3) UNION(UNION中的第二个或后面的SELECT语句)<br>
(4) DEPENDENT UNION(UNION中的第二个或后面的SELECT语句，取决于外面的查询)<br>
(5) UNION RESULT(UNION的结果，union语句中第二个select开始后面所有select)<br>
(6) SUBQUERY(子查询中的第一个SELECT，结果不依赖于外部查询)<br>
(7) DEPENDENT SUBQUERY(子查询中的第一个SELECT，依赖于外部查询)<br>
(8) DERIVED(派生表的SELECT, FROM子句的子查询)<br>
(9) UNCACHEABLE SUBQUERY(一个子查询的结果不能被缓存，必须重新评估外链接的第一行)<br>

- table:输出结果集的表
- partitions:匹配的分区
- type:表示表的连接类型<br>
对表访问方式，表示MySQL在表中找到所需行的方式，又称“访问类型”。<br>
常用的类型有： ALL、index、range、 ref、eq_ref、const、system、NULL（从左到右，性能从差到好）<br><br>
ALL：Full Table Scan， MySQL将遍历全表以找到匹配的行<br>
index: Full Index Scan，index与ALL区别为index类型只遍历索引树<br>
range:只检索给定范围的行，使用一个索引来选择行<br>
ref: 表示上述表的连接匹配条件，即哪些列或常量被用于查找索引列上的值<br>
eq_ref: 类似ref，区别就在使用的索引是唯一索引，对于每个索引键值，表中只有一条记录匹配，简单来说，就是多表连接中使用primary key或者 unique key作为关联条件<br>
const、system: 当MySQL对查询某部分进行优化，并转换为一个常量时，使用这些类型访问。如将主键置于where列表中，MySQL就能将该查询转换为一个常量，system是const类型的特例，当查询的表只有一行的情况下，使用system<br>
NULL: MySQL在优化过程中分解语句，执行时甚至不用访问表或索引，例如从一个索引列里选取最小值可以通过单独索引查找完成。<br><br>


- possible_keys:表示查询时，可能使用的索引
- key:表示实际使用的索引
- key_len:索引字段的长度
- ref:列与索引的比较
- rows:扫描出的行数(估算的行数)
- filtered:按表条件过滤的行百分比
- Extra:执行情况的描述和说明
