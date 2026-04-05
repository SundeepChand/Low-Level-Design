package org.sundeep.distributed.lock;

import org.apache.zookeeper.ZooKeeper;

import java.nio.file.*;
import org.sundeep.distributed.lock.zookeeper.DistributedLock;

public class LockTestDriver {
    private static final String CONNECTION_STRING = "localhost:2181,localhost:2182,localhost:2183";
    private static final int SESSION_TIMEOUT = 5000;
    private static final String LOCK_NAME = "test-lock";
    private static final String FILE_PATH = "shared_counter.txt";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java LockTestDriver <InstanceID>");
            return;
        }

        int instanceId = Integer.parseInt(args[0]);

        // Initialize File if it doesn't exist
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            Files.write(path, "0".getBytes());
        }

        // Connect to ZK
        ZooKeeper zk = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, event -> {
            System.out.println("ZK Event: " + event.getType());
        });

        DistributedLock lock = new DistributedLock(zk, LOCK_NAME);

        System.out.println("Instance " + instanceId + " starting loop...");

        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Instance " + instanceId + " attempting to acquire lock...");
                lock.acquire();
                System.out.println("Instance " + instanceId + " acquired lock. Updating file.");

                // 1. Read current value
                String content = new String(Files.readAllBytes(path)).trim();
                int currentValue = Integer.parseInt(content);

                // 2. Logic: Increment by 1
                int newValue = currentValue + 1;

                // Simulate some "work" while holding the lock
                Thread.sleep(500);

                // 3. Write back
                Files.write(path, String.valueOf(newValue).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Instance " + instanceId + " wrote: " + newValue);

            } finally {
                lock.release();
                System.out.println("Instance " + instanceId + " released lock.");
                // Brief pause so other instances have a chance to grab the lock
                Thread.sleep(200);
            }
        }

        zk.close();
        System.out.println("Instance " + instanceId + " finished.");
    }
}