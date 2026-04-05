#!/bin/bash

# --- CONFIGURATION ---
# Path to your IntelliJ output folder (usually out/production/ProjectName)
# Change 'DistributedLockProject' to your actual project name
CLASSPATH="/Users/sundeepchand/Documents/Low-Level-Design/SystemDesignMasterClass/target/classes"

# Point to where your ZooKeeper dependencies are located (if you have them as JARs in a lib folder)
ZK_LIBS="/Users/sundeepchand/.m2/repository"

# Combine them for the Java command
FULL_CP=".:$CLASSPATH:$ZK_LIBS"

# The Main Class name (including package if applicable)
MAIN_CLASS="org.sundeep.distributed.lock.LockTestDriver"

echo "Clearing old counter file..."
echo "0" > shared_counter.txt

echo "Starting 3 instances of Distributed Lock Driver..."

# Start Instance 0, 1, and 2 in the background
# We redirect output to individual log files so you can inspect them
mvn exec:java -Dexec.mainClass="org.sundeep.distributed.lock.LockTestDriver" -Dexec.args="0" > instance_0.log 2>&1 &
mvn exec:java -Dexec.mainClass="org.sundeep.distributed.lock.LockTestDriver" -Dexec.args="1" > instance_1.log 2>&1 &
mvn exec:java -Dexec.mainClass="org.sundeep.distributed.lock.LockTestDriver" -Dexec.args="2" > instance_2.log 2>&1 &

echo "Instances are running. Use 'tail -f instance_0.log' to watch progress."
echo "Waiting for processes to finish..."

wait

echo "--- TEST COMPLETE ---"
echo "Final value in shared_counter.txt:"
cat shared_counter.txt