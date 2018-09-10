package com.zzc.framelearn.zookeeper;

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
    public void createNodeWithAuth() throws Exception {
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
}
