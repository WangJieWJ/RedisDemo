# RedisDemo
redis是一个著名的key-value存储系统，而作为其官方推荐的Java客户端Jedis页非常强大和稳定，支持事物、管道及有Jedis自身实现的分布式。

# Jedis常见操作 主要包括常用的列表(list)、集合(set)、有序集合(sorted set)、哈希表(hash)等结构，以及其他特性支持。
## 1、使用List：使用列表来模拟队列(queue)、堆栈(stack)，并且支持双向的操作(L或者R)。
### 1、右边入队、右边出队：
```java
jedis.rpush("userList","value");
jedis.rpop("userList");
```

### 2、左边入队、左边出队
```java
jedis.lpush("userList","value");
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
```

