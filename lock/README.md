# AQS 队列同步器
+ NODE 同步队列 (FIFO 双向队列) 
  * **参数**
    1.  waitStatus
       * SIGNAL ： -1， 后继节点的线程处于等待状态，而当前节点的线程如果释放了同步状态或者取消，将会通知后继节点，使后继节点的线程得以运行。
       * CANCELLED ： 1 ，当前节点已经取消获取中断，需要从同步队列中取消等待，节点进入该状态不会变化
       * CONDITION ：-2，  <u>节点在等待队列中，节点线程等待在Condition上，当其他线程对Condition 调用signal()方法后，该节点将会从等待队列中转移到同步队列中，加入对同步状态的获取中(没太理解)</u>
       * PROPAGATE ： -3， <u>表示下一次共享式同步状态将会无条件被传播下去(没太理解)</u>
       * 0 初始状态
    2. 结构图 
      <br> ![RUNOOB 图标](https://github.com/ShanShuan/myBlog/blob/master/lock/src/main/resources/node.jpg)
    