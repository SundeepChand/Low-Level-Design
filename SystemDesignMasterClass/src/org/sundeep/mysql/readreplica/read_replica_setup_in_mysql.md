# Steps to setup read replica in MySQL

Following are the steps to setup read replica by taking a dump of primary and restoring it in replica.
This incurs write downtime on primary, but is the easiest way to setup replication. This is suitable for small databases.

## Configure primary DB & generate sql dump

Edit my.cnf (not needed if using docker compose)
```conf
[mysqld]
server-id=1
log_bin=mysql-bin
binlog_format=ROW
```

Create replication user (on primary)
```sql
-- Create User for replication
CREATE USER 'repl'@'%' IDENTIFIED BY 'replpassword';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;
```

Lock & Get Binary Log Position
```sql
FLUSH TABLES WITH READ LOCK; -- Take a read lock, so that a data dump can be taken
SHOW BINARY LOG STATUS;
```

Result of SHOW BINARY LOG STATUS will give you the current binary log file and position, which will be used to configure replica.
```sql
mysql> SHOW BINARY LOG STATUS;
+------------------+----------+--------------+------------------+-------------------------------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set                         |
+------------------+----------+--------------+------------------+-------------------------------------------+
| mysql-bin.000001 |      158 |              |                  | 331a17e6-06bc-11f1-95e0-e2a691dcc263:1-12 |
+------------------+----------+--------------+------------------+-------------------------------------------+
1 row in set (0.006 sec)
```

Take Data Snapshot
```bash
mysqldump -u root -p --all-databases --master-data > dump.sql
```

Unlock the tables in primary
```sql
UNLOCK TABLES;
```

## Restore the dump in the replica & start replication

Edit my.cnf (not needed if using docker compose)
```conf
[mysqld]
server-id=2
relay-log=relay-bin
read_only=1
```

Import dump on replica
```bash
mysql -u root -p < dump.sql
```

Configure replica to follow primary
```sql
CHANGE REPLICATION SOURCE TO 
SOURCE_HOST='host.docker.internal',
SOURCE_USER='repl',
SOURCE_PASSWORD='replpassword',
SOURCE_LOG_FILE='mysql-bin.000001',
SOURCE_LOG_POS=158;

START REPLICA;
```

Check status
```sql
SHOW REPLICA STATUS;
```
