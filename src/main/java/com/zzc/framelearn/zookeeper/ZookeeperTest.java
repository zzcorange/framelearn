package com.zzc.framelearn.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.util.ArrayList;
import java.util.List;

public class ZookeeperTest {
    public static void main(String...args) throws  Exception{
        String hostPort = "119.23.74.163:2181";
        String zpath = "/";
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
                ACLProvider aclProvider = new ACLProvider();
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
                zk.create("/zktest","aa".getBytes(),acls,CreateMode.PERSISTENT);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
