# RedisDemo

# Redis简介
## NOSQL简介
```
NoSQL是Not-Only-SQL的缩写，是被设计用来替换传统的关系型数据库在某些领域使用，特别针对web2.0站点以及大型的SNS网站，用来满足高并发、大数据的应用需求。
常见的NoSQL数据库系统有HBase(Hadoop数据库，基于列存储)、MonGDB(文档型数据库，采用类型与JSON的BSON语法存储记录)、Redis/Memcached(键值存储数据库)等类型。
```

## Redis介绍
```
Redis是NoSQLogic系统数据库中，和Memcached最为相似的数据库系统，同属键值存储系统。严格意义上来讲，Memcached并不能算作数据库系统，只能算作中间缓存系统，
因为其并不能进行数据的持久化存储。Redis的字面意思是：远程字典服务器(REmote DIctionary Server)，和Memcached相比较，提供了更加丰富的数据类型微笑，更被认为是一种数据结构服器。
```

## Redis、memcached比较
```
和Memcached相比，Redis的优势十分明显。
1. 数据类型：Redis支持更丰富的数据类型，包括字符串(string)、列表(list：可用作队列、堆栈)、集合(set：可以进行集合的运算)、有序集合(sorted set)、哈希表(hash)等，而Memcached仅支持字符串。
2. 对象大小：Redis支持的对象大小最大支持1GB，而Memcached仅为1MB，仅从这个角度来讲，就很有使用Redis替换Memcached的必要。
3. 分片(Sharding)：可以将数据离散地存储在不同的物理机器上，以克服单台机器的内存大小限制。Memcached是在服务器实现实现分片的，而Redis需要借助于Jedis实现客户端分片，Jedis是Redis官方推荐的使用Java访问Redis的方式。
   使用Jedis的分片机制，存储一批数据，在不同的Redis服务器上存储着这批数据的不同部分.而这对客户端来说，而完全透明的，看不到这种差别。另外需要注意，使用Spring Data Redis进行客户端操作时，不提供对分片支持。
4. 持久化：Redis能够将添加到内存中的数据持久化到磁盘，而Memcached则只能充当一个功能相对有限的缓存中间件角色。
```

## 应用场景
```
    1. 数据库服务器：用来存储结构相对简单的的数据。
    2. 缓存系统：缓存需要大量读取、少量修改的数据。
    3. 构建实时消息系统：利用发布(Pub)/订阅(Sub)特性。
    4. 对队列的支持：基于列表(list)实现队列(queue)、堆栈(stack)。
```

# 常见操作命令
Redis提供了丰富的命令，允许我们连接客户端对其进行直接操作，这里简单介绍一下作为常用的一些命令，包括对字符串、
列表、集合、有序集合、哈希表的操作，以及一些其他常用命令

## 基本操作
### 1、添加记录：通常用于设置字符串类型，或者整数类型；如果key已经存在，则覆盖其对应的值。
```java
set name James
```

### 2、获取记录：通过键获取值
```java
get name
```

### 3、递增/递减：针对整数类型，仍然使用类似于字符串的操作：并且可以进行递增、递减操作。
```java
set age 22;
incr age; // age变为23
decr age; // age变为21
```

### 4、在key不存在时才添加
```java
setnx name Nick
```

### 5、设置失效时间：以避免数量的持久增长，如下命令：设置过期时间为5s
```java
setex name 5 Bill;
//上面的命令等价于
set name Bill;
expire name 5;
```

## 列表操作 可以使用列表(list)来模拟队列(query)/堆栈(stack)
### 1、添加元素：给列表userList从右边压入字符串James。
```java
rpush userList James
```

### 2、移除元素：从userList左侧移除按第一个元素
```java
lpop userList
```

### 3、列表范围：如下命令获取从0(左侧起始位置)到-1(右侧最后一个位置)之间的所有元素，并且包含起始位置的元素。
```java
lrange userList 0 -1
```

### 4、设置元素：设置userList位置1处为新值，对包含空格的字符串的字符串使用引号括起来
```
lset userList 1 "Wang Jie"
```

### 5、列表长度
```java
llen userList
```

### 6、裁剪列表：执行如下命令后，列表userList只包含原始列表1到3的连续元素。
```java
ltrim userList 1 3
```

## 集合操作  集合中元素不能重复，并且集合是无序的
### 1、添加元素：可同时添加多个元素
```java
sadd fruit watermelon
sadd fruit apple pear
```

### 2、查看集合中的所有元素
```java
smembers fruit
```

### 3、移除元素
```java
srem fruit apple
```

### 4、集合大小：返回集合中包含的元素的个数
```java
scard fruit
```

### 5、集合总是否包含元素
```shell
sismember fruit pear
```

### 6、集合的运算：如下命令返回集合food和fruit的并集，另外还有交集(sinter)、差集(sdiff)运算。
```shell
sunion food fruit
```

## 有序集合  sorted set
### 1、 添加元素：根据第二个参数进行排序。
```shell
zadd user 23 James
```

### 2、重复添加：存在相同的value，权重参数更新为24。
```shell
zadd user 24 James
```

### 3、集合范围：找到从0到-1的所有元素，并且是有序的。
```shell
zrange user 0 -1
```

## 哈希表操作
### 1、添加元素：给哈希表china添加键为shannxi，值为xian的成员。
```shell
hset china shannxi xian
```

### 2、获取元素：获取哈希表china中键shannxi所对应的value值。
```shell
hget china shannxi
```

### 3、返回哈希表所有的key
```shell
hkeys china
```

### 4、返回哈希表所有的value
```shell
hvals china
```

### 5、清空当前数据库
```shell
flushdb
```

### 6、清空所有数据库
```shell
flushall
```

### 7、测试连接：返回pong即为连接畅通。
```shell
ping
```

### 8、退出客户端：或者是exit命令
```shell
quit
```

### 9、关闭服务器
```shell
shutdown
```

# 常见参数配置  Redis的一些常见设置都是通过对redis.conf文件，进行修改来完成的，一下主要介绍了设置访问密码、主从配置、设置数据和日志目录、以及参数调优等方面
## 一、权限配置
### 1、修改redis.conf文件(Windows系统中是redis.windows.conf)
```shell
requirepass WangJie;  //在配置文件总全文搜素requirepass，将其取消注释，并修改后面的字符串(密码)
```

### 2、 客户端登录，需要先进行授权操作，提供密码即可。
```shell
auth WangJie;
```

## 二、主从配置  通过设置Redis的配置文件redis.conf可以进行主从(Master-Slave)设置，可以设置一个Redis节点为Master，同时设置一个或多个Slave节点。
### 1、在从节点配置redis.conf即可：设置为主节点的IP和端口
```shell
slaveof 192.168.142.12 6379
```

### 2、如果Master节点设置了密码，Slave节点需要同时设置
```
masterauth WangJie
```

### 3、说明
### @通过主从设置，可以进行读写分离：通常Master节点负责写数据，Slave节点负责读数据、注意Slave节点不能进行写操作。
### @数据备份：在Slave节点执行如下命令，然后拷贝dump.rdb即可
```shell
bgsave  //该命令在后台执行，进行持久化操作，不会影响客户端的链接
save    //如果上述bgsave执行失败，可以使用save进行操作，但是会影响客户端的链接
```

## 三、日志/数据目录
### 1、创建如下所示的目录：
```shell
mkdir -p /opt/redis/logs
mkdir -p /opt/redis/data
```

### 2、对日志进行设置：
```shell
loglevel debug                           //日志级别：默认为notice
logfile  /opt/redis/logs/redis.log       //日志输出：默认为stdout
```

### 3、设置数据目录
```shell
dbfilename redis.rdb                       //默认为dump.rdb
dir /opt/redis/data                        //默认为./
```

## 四、设置最大内存
```shell
maxmemory 256mb
#@ 设置Redis能够占用的最大内存，防止影响性能甚至造成系统崩溃。
#@ 一定要小于物理内存(512MB)，留有充足的内存供系统及其他应用程序使用。
```

## 五、备份策略
### 1、使用快照：snapshot
```shell
save 60 1000
#@ 如上的设置，会在60s内、如果有1000个key发生改变就进行持久化。
#@ 可设置多个save选项，默认持久化到dump.rdb。
```

### 2、文件追加(aof)：append-only-file模式
### @Redis会将每个接收到的“写命令”通过write函数追加到appendonly.aof文件，重启Redis时通过该文件重建整个数据库。
### @由于os内核会缓存write函数所做的“修改”，可以使用fsync函数指定写入到磁盘的方式。
```shell
appendonly yes          #启动aof持久化方式

appendfsync always      #对每条“写命令”立即写至磁盘
appendfsync everysec    #默认：每秒写入一次，在性能和可靠性之间的平衡
appendfsync no          #依赖于os，不指定写入时机
```

### 3、两种方式的比较：
```
### @快照方式：性能较好，但是快照间隔期间，如果宕机将造成数据丢失。
### @AOF模式：影响性能，不容易造成数据丢失。
### @如果Redis宕机：重启Redis即可，会自动使用redis.rdb、appendonly.aof恢复数据库。
```

### 4、主从备份：从数据安全性角度考虑
1. 关闭快照功能。
2. 同时设置主从服务器都为AOF模式。
3. 说明：如果仅对Slave进行持久化设置，重启时，Slave自动和Master进行同步，全部数据丢失


初识Jedis
http://hello-nick-xu.iteye.com/blog/2076890


redis是一个著名的key-value存储系统，而作为其官方推荐的Java客户端Jedis页非常强大和稳定，支持事物、管道及有Jedis自身实现的分布式。

# Jedis常见操作 主要包括常用的列表(list)、集合(set)、有序集合(sorted set)、哈希表(hash)等结构，以及其他特性支持。
## 1、使用List：使用列表来模拟队列(queue)、堆栈(stack)，并且支持双向的操作(L或者R)。
### 1、右边入队、右边出队,可同时存储多个：
```shell
jedis.rpush("userList","value");
jedis.rpush("userList","value1","value2");
jedis.rpop("userList");
```

### 2、左边入队、左边出队
```shell
jedis.lpush("userList","value");
jedis.lpush("userList","value1","value2");
jedis.lpop("userList");
```

### 3、返回列表范围：从0开始，到最后一个(-1)[包含]
```shell
List<String> userList=jedis.lrange("userList",0,-1);
```

### 4、删除:使用Key
```shell
jedis.del("userList");
```

### 5、设置：位置1出为新值
```shell
jedis.lset("userList",1,"Nick Xu");
```

### 6、返回长度：
```shell
Long size=jedis.llen("userList");
```

### 7、进行裁剪：包含
```shell
jedis.ltrim("userList",1,2); //第二个参数为开始索引，第三个参数为结束索引
```

## 2、使用set，和列表不同，集合中的元素是无序的，因此元素也不能重复。
### 1、添加到set：可一次添加多个
```shell
jedis.sadd("fruit","apple");
jedis.sadd("fruit","pear","watermelon");
```

### 2、遍历集合：
```shell
Set<String> fruit=jedis.smembers("fruit");
```

### 3、移除元素：remove
```shell
jedis.srem("fruit","pear");
```

### 4、返回长度
```shell
Long size=jedis.scard("fruit");
```

### 5、是否包含
```shell
Boolean isMember=jedis.sismember("fruit","pear");
```

### 6、集合的操作:包括集合的交运算(sinter)、差集(sdiff)、并集(sunion)
```shell
jedis.sadd("food","bread","milk");
Set<String> fruitFood=jedis.sunion("fruit","food");
Set<String> fruit_food=jedis.sdiff("fruit","food");   //此表达式中使用的sdiff表示的是，第一个Set集合(fruit)中含有而在第二个Set集合(food)中不存在的元素。
                                                      //如果想获取两个Set集合中独有的元素时，就可以两次使用sdiff。在对生成的两个集合求并即可。
```

## 3、使用sorted set：有序集合在集合的基础上，增加一个排序的参数。
### 1、有序集合：根据"第二个参数"进行排序。
```shell
jedis.zadd("user",22,"James");
```

### 2、再次添加：元素相同时，更新为当前的权重。
```shell
jedis.zadd("user",24,"James");   //此时James的权重为24
```

### 3、zset的范围：找到从0到-1(-1表示最后一位。)的所有元素。
```shell
Set<String> user=jedis.zrange("user",0,-1);   //表示全部元素，当不知道集合中元素的多少的时候，可以使用-1来表示最后一位。
```

### 4、实际上此处的排序集合的类型是LinkedHashSet。

## 4、使用Map
### 1、保存Map数据
```shell
Map<String,String> capital=new HashMap<String,String>();
capital.put("shanXi",xiAn);
capital.put("shanDong","jiNan");
capital.put("beiJing","beiJing");
capital.put("heBei","shiJiaZhuang");
jedis.hmset("capitail",capital);
```

### 2、获取数据
```shell
List<String> cities = jedis.hmget("capital", "shannxi", "shanghai");
```

## 5、其他操作：
### 1、对key的操作：
#### 对Key的模糊查询：
```shell
Set<String> keys=jedis.keys("*");
Set<String> keys=jedis.keys(user.userid.*);
```

#### 删除key
```shell
jedis.del("city");
```
#### 是否存在
```shell
Boolean isExists=jedis.exists("user.userid.14101");
```

### 2、失效时间
#### expire:时间为5s
```shell
jedis.setex("user.userid.14101",5,"James");
//上面等价于
//jedis.set("user.userid.14101","James");
//jedis.expire("user.userid.14101", 5);
```

#### 存活时间(ttl):time to live
```shell
Long seconds=jedis.ttl("user.userid.14101");
```

#### 去掉key的expire设置：不在有失效时间
```shell
jedis.persist("user.userid.14101");
```

#### 如果已经给一个key设置过失效时间，再从为该key赋值，则此时失效时间就会没用了。
```shell
jedis.setex("Key",10,"Value");
jedis.set("Key","Value1");   //此时Key就不在具有失效时间。
```

### 3、自增的类型：
#### 1、int类型采用string类型的方式存储
```shell
jedis.set("amount",100+"");
```

#### 2、递增或递减：incr()/decr()
```shell
jedis.incr("amount");  //每次增加1
jedis.decr("amount");  //每次减少1
```

#### 3、增加或减少
```shell
jedis.incrBy("amount",20); //增加20
jedis.decrBy("amount",10); //减少10
```

### 4、数据清空
#### 1、清空当前的db
```shell
jedis.flushDB();
```

#### 2、清空所有db
```shell
jedis.flushAll();
```

### 5、事务支持
#### 1、获取事务
```shell
Transaction tx=jedis.multi();
```

#### 2、批量操作：tx采用和jedis一致的API接口
```shell
for(int i=0;i<10;i++){
    tx.set("key"+i,"value"+i);
    System.out.println("------------key"+i);
    Thread.sleep(1000);
}
```

#### 3、执行事务：针对每一个操作，返回其执行的结果，成功即为OK
```shell
List<Object> results=tx.exec();
```


//学习地址
http://hello-nick-xu.iteye.com/blog/2075670
http://hello-nick-xu.iteye.com/blog/2077243?utm_source=tuicool&utm_medium=referral