package com.zzc.framelearn.zookeeper;

import java.util.concurrent.CountDownLatch;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;
public class Conf {
    public static void waitUntilConnected(ZooKeeper zooKeeper, CountDownLatch connectedLatch) {
        if (States.CONNECTING == zooKeeper.getState()) {
            try {
                connectedLatch.await();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    static class ConnectedWatcher implements Watcher {

        private CountDownLatch connectedLatch;

        ConnectedWatcher(CountDownLatch connectedLatch) {
            this.connectedLatch = connectedLatch;
        }

        @Override
        public void process(WatchedEvent event) {
            if (event.getState() == KeeperState.SyncConnected) {
                connectedLatch.countDown();
            }
        }
    }

    static public Conf Instance() {
        if (static_ == null) {
            static_ = new Conf();
        }
        return static_;
    }

    public boolean Init(String hostports, int times) {
        try {
            CountDownLatch connectedLatch = new CountDownLatch(1);
            Watcher watcher = new ConnectedWatcher(connectedLatch);
            zk_ = new ZooKeeper(hostports, times, watcher);
            waitUntilConnected(zk_, connectedLatch);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public String Get(String keys) {
        String re = "";
        String ppath = "/zookeeper";
        int oldpos = -1;
        int pos = 0;
        while (true) {
            pos = keys.indexOf(".", oldpos + 1);
            if (pos < 0) {
                ppath += "/";
                String str = keys.substring(oldpos + 1);
                ppath += str;
                break;
            }
            ppath += "/";
            String str = keys.substring(oldpos + 1, pos);
            ppath += str;
            oldpos = pos;
        }
        Stat stat = new Stat();
        try {
            byte[] b = zk_.getData(ppath, false, stat);    //获取节点的信息及存储的数据
            re = new String(b);
        } catch (Exception e) {
            System.out.println(e);
        }
        return re;
    }

    private Conf() {

    }

    private ZooKeeper zk_;
    static private Conf static_;

    public static void main(String args[]) {
        String hostports = "119.23.74.163:2181,119.23.74.163:2182,119.23.74.163:2183";

        Conf.Instance().Init(hostports, 1000);

        String str = Conf.Instance().Get("conf.logicpoint.subscriberserverip");
        str = Conf.Instance().Get("conf.logicpoint.subscriberserverport");
        System.out.println(str);
        while (true) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }

    }
}
