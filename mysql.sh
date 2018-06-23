#!/bin/bash
docker stop mysql
docker rm mysql
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mysql -d mysql:5.7
