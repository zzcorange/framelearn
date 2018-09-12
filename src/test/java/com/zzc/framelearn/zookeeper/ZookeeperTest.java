package com.zzc.framelearn.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZookeeperTest {
    private static  String hostPort = "119.23.74.163:2181,119.23.74.163:2182,119.23.74.163:2183";
    private static String zpath = "/";
    @Test
    public void test(){
        System.out.println("tomer");
    }
    @Test
    public void testCreateWithOutAuth() throws  Exception{
        List<String> zooChildren = new ArrayList<String>();
        ZooKeeper zk = new ZooKeeper(hostPort, 2000, null);
        if (zk != null) {
            try {
                zooChildren = zk.getChildren(zpath, false);
                System.out.println("Znodes of '/': ");
                for (String child: zooChildren) {
                    //print the children
                    System.out.println(child);
                }
                List<ACL> acls = new ArrayList<ACL>();
                for (ACL ids_acl : ZooDefs.Ids.READ_ACL_UNSAFE) { //遍历出所有权限类型
                    System.out.println(ids_acl.toString());
                    System.out.println(ids_acl.getPerms());
                    acls.add(ids_acl);
                }
                for(ACL ids_acl:ZooDefs.Ids.READ_ACL_UNSAFE){
                    acls.add(ids_acl);
                }
                ACL acl = new ACL();
                zk.create("/zktest","aa".getBytes(),acls, CreateMode.PERSISTENT);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void readNodeWithAuth() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(hostPort, 2000, null);
//        zooKeeper.setACL("/zktestAuth");
       // zooKeeper.addAuthInfo("",);
        List<String> zooChildren = new ArrayList<String>();
      // zooKeeper.getChildren("/zktest",true,new MyChildren2Callback(),null);
        /**
         * 增加授权
         */
        zooKeeper.addAuthInfo("digest","test:test".getBytes());
        zooChildren  =  zooKeeper.getChildren("/zktest/zktt",false);
       for(String zoo:zooChildren){
           System.out.println(zoo);
       }
       Stat stat  = new Stat();
        byte[] datas = zooKeeper.getData("/zktest/zktt",new ZookeeperWatcher(),stat);
        System.out.println("stat:"+stat.toString());
        System.out.println("pathvalue:"+new String(datas));
        System.out.println(stat.getEphemeralOwner());
        /**
         * 测试ip授权的数据
         */
        datas = zooKeeper.getData("/iptest",new ZookeeperWatcher(),stat);
        System.out.println("stat:"+stat.toString());
        System.out.println("pathvalue:"+new String(datas));
        System.out.println(stat.getEphemeralOwner());



    }

    @Test
    public void createNode() throws  Exception{
        Watcher watcher = new MyWatcher();
        ZooKeeper zooKeeper = new ZooKeeper(hostPort,2000,watcher);
      //  zooKeeper.create("/createnodetestpersistent","createnodetest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
   //     zooKeeper.register(watchedEvent -> {System.out.println(watchedEvent.toString());});
       byte[] b= zooKeeper.getData("/zktest",true,new Stat());
       System.out.println(new String(b));
        System.in.read();
    }

    /**
     * 测试分布式锁
     */
    @Test
    public void testLock()throws  Exception{
        //创建zookeeper的客户端

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);

        client.start();

//创建分布式锁, 锁空间的根节点路径为/curator/lock

        InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
        System.out.println("before mutex");

        mutex.acquire();

//获得了锁, 进行业务流程

        System.out.println("Enter mutex");
        for(;;){
            Thread.sleep(1000);
            if(1>2) break;
        }
//完成业务流程, 释放锁

        mutex.release();

//关闭客户端

        client.close();
    }
}
