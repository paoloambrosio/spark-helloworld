# Docker Compose to test the applications

## Exposed Services:

 - Spark UI: http://localhost:8080/
 - HDFS UI: http://localhost:50070/

## Operations

### HDFS

```
$ docker-compose exec hdfs-datanode bash
# /opt/hadoop-2.7.4/bin/hdfs dfs -mkdir /helloworld
# /opt/hadoop-2.7.4/bin/hdfs dfs -copyFromLocal mylocalfile /helloworld/myremotefile
```

## Credits

Copied and adapted from:

 - https://github.com/big-data-europe/docker-hadoop
 - https://github.com/big-data-europe/docker-spark
 - https://github.com/big-data-europe/docker-hadoop-spark-workbench
