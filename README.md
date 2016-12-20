# RedisDemo
redis是一个著名的key-value存储系统，而作为其官方推荐的Java客户端Jedis页非常强大和稳定，支持事物、管道及有Jedis自身实现的分布式。

# Jedis常见操作 主要包括常用的列表(list)、集合(set)、有序集合(sorted set)、哈希表(hash)等结构，以及其他特性支持。
## 1、使用List：使用列表来模拟队列(queue)、堆栈(stack)，并且支持双向的操作(L或者R)。
### 1、右边入队、右边出队,可同时存储多个：
```java
jedis.rpush("userList","value");
jedis.rpush("userList","value1","value2");
jedis.rpop("userList");
```

### 2、左边入队、左边出队
```java
jedis.lpush("userList","value");
jedis.lpush("userList","value1","value2");
jedis.lpop("userList");
```

### 3、返回列表范围：从0开始，到最后一个(-1)[包含]
```java
List<String> userList=jedis.lrange("userList",0,-1);
```

### 4、删除:使用Key
```java
jedis.del("userList");
```

### 5、设置：位置1出为新值
```java
jedis.lset("userList",1,"Nick Xu");
```

### 6、返回长度：
```java
Long size=jedis.llen("userList");
```

### 7、进行裁剪：包含
```
jedis.ltrim("userList",1,2); //第二个参数为开始索引，第三个参数为结束索引
```

## 2、使用set，和列表不同，集合中的元素是无序的，因此元素也不能重复。
### 1、添加到set：可一次添加多个
```java
jedis.sadd("fruit","apple");
jedis.sadd("fruit","pear","watermelon");
```

### 2、遍历集合：
```java
Set<String> fruit=jedis.smembers("fruit");
```

### 3、移除元素：remove
```java
jedis.srem("fruit","pear");
```

### 4、返回长度
```java
Long size=jedis.scard("fruit");
```

### 5、是否包含
```java
Boolean isMember=jedis.sismember("fruit","pear");
```

### 6、集合的操作:包括集合的交运算(sinter)、差集(sdiff)、并集(sunion)
```java
jedis.sadd("food","bread","milk");
Set<String> fruitFood=jedis.sunion("fruit","food");
Set<String> fruit_food=jedis.sdiff("fruit","food");   //此表达式中使用的sdiff表示的是，第一个Set集合(fruit)中含有而在第二个Set集合(food)中不存在的元素。
                                                      //如果想获取两个Set集合中独有的元素时，就可以两次使用sdiff。在对生成的两个集合求并即可。
```

## 3、使用sorted set：有序集合在集合的基础上，增加一个排序的参数。
### 1、有序集合：根据"第二个参数"进行排序。
```java
jedis.zadd("user",22,"James");
```

### 2、再次添加：元素相同时，更新为当前的权重。
```java
jedis.zadd("user",24,"James");   //此时James的权重为24
```

### 3、zset的范围：找到从0到-1(-1表示最后一位。)的所有元素。
```java
Set<String> user=jedis.zrange("user",0,-1);   //表示全部元素，当不知道集合中元素的多少的时候，可以使用-1来表示最后一位。
```

### 4、实际上此处的排序集合的类型是LinkedHashSet。

## 4、使用Map
### 1、保存Map数据
```java
Map<String,String> capital=new HashMap<String,String>();
capital.put("shanXi",xiAn);
capital.put("shanDong","jiNan");
capital.put("beiJing","beiJing");
capital.put("heBei","shiJiaZhuang");
jedis.hmset("capitail",capital);
```

### 2、获取数据
```java
List<String> cities = jedis.hmget("capital", "shannxi", "shanghai");
```

## 5、其他操作：
### 1、对key的操作：
#### 对Key的模糊查询：
```java
Set<String> keys=jedis.keys("*");
Set<String> keys=jedis.keys(user.userid.*);
```

#### 删除key
```java
jedis.del("city");
```
#### 是否存在
```java
Boolean isExists=jedis.exists("user.userid.14101");
```

### 2、失效时间
#### expire:时间为5s
```java
jedis.setex("user.userid.14101",5,"James");
//上面等价于
//jedis.set("user.userid.14101","James");
//jedis.expire("user.userid.14101", 5);
```

#### 存活时间(ttl):time to live
```java
Long seconds=jedis.ttl("user.userid.14101");
```

#### 去掉key的expire设置：不在有失效时间
```java
jedis.persist("user.userid.14101");
```

#### 如果已经给一个key设置过失效时间，再从为该key赋值，则此时失效时间就会没用了。
```java
jedis.setex("Key",10,"Value");
jedis.set("Key","Value1");   //此时Key就不在具有失效时间。
```

### 3、自增的类型：
#### 1、int类型采用string类型的方式存储
```java
jedis.set("amount",100+"");
```

#### 2、递增或递减：incr()/decr()
```java
jedis.incr("amount");  //每次增加1
jedis.decr("amount");  //每次减少1
```

#### 3、增加或减少
```java
jedis.incrBy("amount",20); //增加20
jedis.decrBy("amount",10); //减少10
```

### 4、数据清空
#### 1、清空当前的db
```java
jedis.flushDB();
```

#### 2、清空所有db
```java
jedis.flushAll();
```

### 5、事务支持
#### 1、获取事务
```java
Transaction tx=jedis.multi();
```

#### 2、批量操作：tx采用和jedis一致的API接口
```java
for(int i=0;i<10;i++){
    tx.set("key"+i,"value"+i);
    System.out.println("------------key"+i);
    Thread.sleep(1000);
}
```

#### 3、执行事务：针对每一个操作，返回其执行的结果，成功即为OK
```java
List<Object> results=tx.exec();
```


//学习地址
http://hello-nick-xu.iteye.com/blog/2075670
http://hello-nick-xu.iteye.com/blog/2077243?utm_source=tuicool&utm_medium=referral