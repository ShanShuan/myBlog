# AQS 队列同步器
+ NODE 同步队列 (FIFO 双向队列) 
  * **参数**
    1.  waitStatus
        - SIGNAL ： -1， 后继节点的线程处于等待状态，而当前节点的线程如果释放了同步状态或者取消，将会通知后继节点，使后继节点的线程得以运行。
        - CANCELLED ： 1 ，当前节点已经取消获取中断，需要从同步队列中取消等待，节点进入该状态不会变化
        - CONDITION ：-2，  <u>节点在等待队列中，节点线程等待在Condition上，当其他线程对Condition 调用signal()方法后，该节点将会从等待队列中转移到同步队列中，加入对同步状态的获取中(没太理解)</u>
        - PROPAGATE ： -3， <u>表示下一次共享式同步状态将会无条件被传播下去(没太理解)</u>
        -  0  初始状态
    2. 结构图 
        <br> ![图标](https://github.com/ShanShuan/myBlog/blob/master/lock/src/main/resources/node.jpg)
+ 独占式获取锁
    ```
     public final void acquire(int arg) {
            if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
                selfInterrupt();
        }
    ```
  上述代码主要完成了同步状态获取[tryAcquire(arg)] 节点构造、加入同步队列[addWaiter(Node.EXCLUSIVE)]以及在同步队列中自旋等待的相关工作[acquireQueued(node,arg)]
  <br>其主要逻辑是：首先调用自定义同步器实现的tryAcquire(int arg)方法，该方法保证线程安全的获取同步状态，如果同步状态获取失败，则构造同步节点（独占式Node. EXCLUSIVE，同一时刻只能有一个线程成功获取同步状态）
  并通过addWaiter(Node node)方法将该节点加入到同步队列的尾部，最后调用acquireQueued(Node node, int arg)方法，使得该节点以“死循环”的方式获取同步状态。
  如果获取不到则阻塞节点中的线程，而被阻塞线程的唤醒主要依靠前驱节点的出队或阻塞线程被中断来实现。<br><br>
  同步器的acquireQueued方法
  ```
  final boolean acquireQueued(final Node node, int arg) {
          boolean failed = true;
          try {
              boolean interrupted = false;
              for (;;) {
                  final Node p = node.predecessor();
                  if (p == head && tryAcquire(arg)) {
                      setHead(node);
                      p.next = null; // help GC
                      failed = false;
                      return interrupted;
                  }
                  if (shouldParkAfterFailedAcquire(p, node) &&
                      parkAndCheckInterrupt())
                      interrupted = true;
              }
          } finally {`
              if (failed)
                  cancelAcquire(node);
          }
      }
  ```
  在acquireQueued (final Node node, int arg)方法中，当前线程在“死循环”中尝试获取同步状态，而只有前驱节点是头节点才能够尝试获取同步状态，这是为什么？原因有两个，如下。
  <br>第一，头节点是成功获取到同步状态的节点，而头节点的线程释放了同步状态之后，将会唤醒其后继节点，后继节点的线程被唤醒后需要检查自己的前驱节点是否是头节点  
  <br>第二，维护同步队列的FIFO原则
  独占式同步状态获取流程，也就是acquire(int arg)方法调用流程图下所示：<br>
   ![图标](https://github.com/ShanShuan/myBlog/blob/master/lock/src/main/resources/acc.jpg)<br><br>
   shouldParkAfterFailedAcquire方法有三个作用：<bar>
   * 1、若pred.waitStatus状态位大于0，说明这个节点已经取消了获取锁的操作<bar>
   * 2、若状态位不为Node.SIGNAL,且没有取消操作，则会尝试将状态位修改为Node.SIGNAL<bar>
   * 3、状态位是Node.SIGNAL，表明线程是否已经准备好被阻塞并等待唤醒