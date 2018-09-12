package com.zzc.framelearn.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class MyWatcher implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
          System.out.println(watchedEvent.toString());
    }
}
