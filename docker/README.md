#docker
## 容器
````
#远程拉取ubuntu 镜像到本地
docker pull ubuntu

#运行 一个叫  myubuntu的名字的容器 ，镜像是以 ubuntu 为镜像  -i: 交互式操作 -t: 终端  -d 后台运行 /bin/bash：放在镜像名后的是命令，这里我们希望有个交互式 Shell，因此用的是 /bin/bash
docker run -itd --name myubuntu ubuntu /bin/bash

#显示 正在docker 容器
docker ps 

#显示 所有的docker 容器
docker ps -a

#进入后台运行的容器  --之后的操作 是再容器内的指定
docker  attach 83f112d54806

# 想从docker 容器的交互终端退出  exit (容器会退出运行--不再后台运行)
exit

#如果想让后台 运行容器  快捷键 ctrl+P+Q 

#重启docker id 为8cda00f0915e(CONTAINER ID) 的docker容器
docker restart 8cda00f0915e

#停止容器
docker stop 83f112d54806<容器 ID>

#删除定制容器id 的容器
docker rm -f 83f112d54806

#-p  前面6600 为本机对外端口 后面的6000 为 docker 容器内部端口
docker  run -d -p 6600:6000   training/webapp python app.py

#查看指定 容器id 的网路端口情况
docker port 83f112d54806<容器 ID 或者名字>

#查看容器日志 -f 为了能像 tail -f 一样输出容器内部的日志
docker logs -f 2432b6c03937<容器 ID 或者名字>

#查看WEB应用程序容器的进程
docker top agitated_bardeen<容器 ID 或者名字>

#来查看 Docker 的底层信息
docker  inspect 2432b6c03937<容器 ID 或者名字>

#查询最后一次创建的容器
docker ps -l

# 启动容器 如果要启动 正在运行的容器 使用 docker restart
docker start 2432b6c03937<容器 ID 或者名字>



````

##镜像
````
#显示本地的镜像
docker images

#查找镜像
docker search httpd

#拖取镜像
docker pull httpd

# -t  后面跟 镜像的名字  .表示当前目录下的 dockerfile 文件 ,也可以指绝对路径
docker  build -t wangzifeng/v1 .

#为镜像 id 为 5daf62e50bd3 打上tag  并创建一个新的images 
docker  tag 5daf62e50bd3  wangzifeng/centos:dev


````


##容器连接
````
#容器内部端口绑定到指定的主机端口
-p

#容器内部端口随机映射到主机的高端口
-P

#创建一个新的 Docker 网络  
#-d 参数指定 Docker 网络类型，有 bridge、overlay
# test-net 网络名称
docker network create -d bridge test-net

#运行容器 指定连接的网络
docker run -itd --name test1 --network test-net ubuntu /bin/bash

#查看 docker 网络
docker network ls

````

