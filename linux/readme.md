## sh 脚本
```
#!/bin/bash
echo "Hello World !"
```
1、#! 是一个约定的标记，它告诉系统这个脚本需要什么解释器来执行，即使用哪一种 Shell


## linux 指定大全
```
//查看系统中文件的使用情况
df -h
//查看当前目录下各个文件及目录占用空间大小
du -sh *
//方法一：切换到要删除的目录，删除目录下的所有文件
rm -f *
//方法二：删除logs文件夹下的所有文件，而不删除文件夹本身
rm -rf logs/
//匹配特定目录下的文件或文件夹，然后进行删除
ls *.log | xargs rm -f

统计总数大小
du -sh xmldb/

把/usr/dockerfile 的文件移动到 /wangzifeng/dockerfile
mv /usr/dockerfile /wangzifeng/dockerfile

将下载的文件存放到指定的文件夹下，同时重命名下载的文件
wget -O /home/index http://www.baidu.com

显示网络网卡
 brctl show

```