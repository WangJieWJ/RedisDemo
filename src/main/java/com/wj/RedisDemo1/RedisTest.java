package com.wj.RedisDemo1;

import redis.clients.jedis.Jedis;

/**
 *
 * 使用单链接
 * Created by wangjie on 2016/12/20 0020.
 */
public class RedisTest {

    public static void main(String[] args){
        //创建连接
        String host="127.0.0.1";
        int port=6379;
        Jedis client=new Jedis(host,port);

        long start=System.currentTimeMillis();
        String returnStr="";
        for(int i=0;i<100000;i++){
            returnStr=client.set("n"+i,"正在存储第"+i+"个数"); //MySQL数据库中存储9733条数据，运行了3分钟。
        }
        long end=System.currentTimeMillis();
        System.out.println("Simple SET:"+(end-start)/1000.0+"seconds");

        //执行get指令
        String data=client.get("temp");
        System.out.println("修改之前："+data);

        //执行set指令
        String result=client.set("temp","修改temp的值");
        System.out.println("执行结果："+result);

        //执行get指令
        String value=client.get("temp");
        System.out.println("修改之后："+value);

        client.disconnect();
    }
}
