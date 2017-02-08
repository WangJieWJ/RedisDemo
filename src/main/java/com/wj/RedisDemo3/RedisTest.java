package com.wj.RedisDemo3;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangjie on 2016/12/20 0020.
 */
public class RedisTest {

    public static void main(String[] args) {
        redisSet();
    }

    /**
     * 对List集合的操作
     */
    public static void redisList(){
        String host = "127.0.0.1";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);

        System.out.println("从左边入队列…………");
        jedis.lpush("userList","WJ1");
        jedis.lpush("userList","WJ2");
        jedis.lpush("userList","WJ3");
        jedis.lpush("userList","WJ4");
        jedis.lpush("userList","WJ5");
        jedis.lpush("userList","WJ6");
        jedis.lpush("userList","WJ7");
        List<String> list=jedis.lrange("userList",0,-1);
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }

        System.out.println("将位置1处设置为新值");
        jedis.lset("userList",1,"WangJie");
        List<String> list2=jedis.lrange("userList",0,-1);
        for(int i=0;i<list2.size();i++){
            System.out.println(list2.get(i));
        }

        System.out.println("进行裁剪：包含");
        jedis.ltrim("userList",4,5);
        List<String> list3=jedis.lrange("userList",0,-1);
        for(int i=0;i<list3.size();i++){
            System.out.println(list3.get(i));
        }

        Long Str=jedis.del("userList");
        System.out.println(Str);

        System.out.println(jedis.exists("userList"));

        jedis.disconnect();
    }

    /**
     * 对Set集合的操作
     */
    public static void redisSet(){
        String host="127.0.0.1";
        int port=6379;
        Jedis jedis=new Jedis(host,port);
        jedis.sadd("food","bread","milk");
        jedis.sadd("fruit","apple","orange","bread");
        Set<String> fruitFood=jedis.sdiff("food","fruit");
        for(String s: fruitFood){
            System.out.println(s);
        }
        jedis.disconnect();
    }

    /**
     * LinkedHashSet的操作
     */
    public static void redisZset(){
        String host="127.0.0.1";
        int port=6379;
        Jedis jedis=new Jedis(host,port);
        jedis.zadd("user",23,"WangJie");
        jedis.zadd("user",288,"WangJie");
        jedis.zadd("user",56,"WJ");
        jedis.zadd("user",56,"WJ2");
        jedis.zadd("user",56,"WJ4");
        jedis.zadd("user",5,"WJ");
        Set<String> set=jedis.zrange("user",0,-1);
        for(String s:set){
            System.out.println(s);
        }
        jedis.disconnect();
    }

    /**
     * 对Map集合的操作
     */
    public static void redisMap(){
        String host="127.0.0.1";
        int port=6379;
        Jedis jedis=new Jedis(host,port);

        Map<String,String> capital=new HashMap<String,String>();
        capital.put("shanXi","xiAn");
        capital.put("shanDong","jiNan");
        capital.put("beiJing","beiJing");
        capital.put("heBei","shiJiaZhuang");
        jedis.hmset("capital",capital);
        List<String> cities=jedis.hmget("capital","shanDong","beiJing");
        for(String s:cities){
            System.out.println(s);
        }
        jedis.disconnect();
    }

    /**
     * 自增操作
     */
    public static void autoChange(){
        String host="127.0.0.1";
        int port=6379;
        Jedis jedis=new Jedis(host,port);
        jedis.set("amount",100+"");
        System.out.println(jedis.get("amount"));
        jedis.incr("amount");
        System.out.println(jedis.get("amount"));
        jedis.decr("amount");
        System.out.println(jedis.get("amount"));
        jedis.incrBy("amount",-120);
        System.out.println(jedis.get("amount"));
        jedis.disconnect();
    }

    public static void redisTx(){
        try{
            String host="127.0.0.1";
            int port=6379;
            Jedis jedis=new Jedis(host,port);
            Transaction tx=jedis.multi();
            for(int i=0;i<10;i++){
                tx.set("key"+i,"value"+i);
                System.out.println("------------key"+i);
                Thread.sleep(1000);
            }
            List<Object> results=tx.exec();
            System.out.println(jedis.get("key1"));
            for(Object s:results){
                System.out.println(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过redis缓存来防止并发
     * redis-Lock
     */
    public static void redisLock(){
        String host = "127.0.0.1";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        //定义开始时间。
        Long start = System.currentTimeMillis();
        //保存Key值。
        jedis.set("WJ_LOCK", "DEMO");
        //设置Key值的生命周期为5，单位为秒
        jedis.expire("WJ_LOCK", 5);

        // 如果设置
        // jedis.set("WJ_LOCK", "DEMO");
        boolean exist = true;
        while (exist) {
            exist = jedis.exists("WJ_LOCK");
            System.out.println("存在！！！" + exist);
        }
        //
        //定义结束时间。
        Long end = System.currentTimeMillis();
        System.out.println("Key失效" + exist + (end - start) / 1000.0);
        jedis.close();
    }

    public void Hello(String[] args){
        String Str="12404313WJasfda404sdWJsadfdf102";
        Str.indexOf("404");
    }
}
