package com.wj.RedisDemo2;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 使用连接池+分布式
 * Created by wangjie on 2016/12/20 0020.
 */
public class RedisTest {

    public static void main(String[] args){
        //生成多级连接信息列表
        List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1",6379));

        //生成连接池配置信息
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(30);
        config.setMaxWaitMillis(3*1000);

        //在应用初始化的时候生成连接池
        ShardedJedisPool pool=new ShardedJedisPool(config,shards);

        ShardedJedis client=pool.getResource();
        try{
            //执行指令
            String result=client.set("Key-String","Hello,Redis!");
            System.out.println(result);
            String value=client.get("Key-String");
            System.out.println(value);
            String Temp=client.get("temp");
            System.out.println(Temp);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //业务操作完成，将连接返回给连接池
            if(null != client){
                //此方法已经被弃用，但是新的方法只能从3.0开始使用，现在使用的是2.9.0，所以只能使用此方法。
                pool.returnResource(client);
            }
        }

        //应用关闭时，释放连接池资源
        pool.destroy();
    }
}
