package com.zookeeper.domain;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import sun.awt.image.SunWritableRaster;
import sun.rmi.runtime.NewThreadAction;

/**
 * @Author LT-0024
 * @Date 2020/10/23 14:52
 * @Version 1.0
 */
public class Zookeeper {
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.
                newClient("39.105.221.247:2181",12000,10000,new ExponentialBackoffRetry(1000,3));
        //建立客户端连接
        curatorFramework.start();
        InterProcessMutex lock = new InterProcessMutex(curatorFramework,"/locks");
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"-->尝试获取锁");
                try {
                    //抢占锁操作
                    lock.acquire();
                    System.out.println(Thread.currentThread().getName()+"-->获得锁成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                    lock.release();
                    System.out.println(Thread.currentThread().getName()+"-->释放锁成功");
                }catch (Exception e){
                    e.printStackTrace();
                }
            },"T"+i).start();
        }
    }
}
