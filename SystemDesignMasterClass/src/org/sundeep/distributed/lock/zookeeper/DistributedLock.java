package org.sundeep.distributed.lock.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DistributedLock {
    private final ZooKeeper zk;
    private final String lockPath;
    private String selfNode;

    public DistributedLock(ZooKeeper zk, String lockName) {
        this.zk = zk;
        this.lockPath = "/" + lockName;

        try {
            if (zk.exists(lockPath, false) == null) {
                zk.create(lockPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create lock path", e);
        }
    }

    public void acquire() throws KeeperException, InterruptedException {
        selfNode = zk.create(lockPath + "/acq-n_", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        while (true) {
            List<String> children = zk.getChildren(lockPath, false);
            Collections.sort(children);

            String myNodeName = selfNode.substring(lockPath.length() + 1);
            int myIndex = children.indexOf(myNodeName);

            if (myIndex == 0) {
                // We are the smallest! Lock acquired
                return;
            } else {
                // Watch the node just before us
                String currentWaitNode = lockPath + "/" + children.get(myIndex - 1);
                CountDownLatch latch = new CountDownLatch(1);

                Stat stat = zk.exists(currentWaitNode, event -> {
                    if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                        latch.countDown();
                    }
                });

                if (stat != null) {
                    latch.await(); // Block the current thread until the previous node is deleted
                }
                // If stat is null, it means the node was already deleted, so we can try to acquire the lock again
            }
        }
    }

    public void release() throws KeeperException, InterruptedException {
        if (selfNode != null) {
            zk.delete(selfNode, -1);
            selfNode = null;
        }
    }
}
