# study-paxos #
ref: https://my.oschina.net/u/150175/blog/2992187



在paxos算法中，分为4种角色



 Proposer ：提议者

 Acceptor：决策者

 Client：产生议题者

 Learner：最终决策学习者





**P0. 当集群中，超过半数的Acceptor接受了一个议案，那我们就可以说这个议案被选定了（Chosen）。**



**P1：一个Acceptor必须接受它收到的第一个议案。**



**P2：如果一个值为v的议案被选定了，那么被选定的更大编号的议案，它的值必须也是v。**

**P2a：如果一个值为v的议案被选定了，那么Acceptor接受的更大编号的议案，它的值必须也是v。**

**P2b：如果一个值为v的议案被选定了，那么Proposer提出的更大编号的议案，它的值必须也是v。**



**P2c：在所有Acceptor中，任意选取半数以上的Acceptor集合，我们称这个集合为S。Proposal新提出的议案（简称Pnew）必须符合下面两个条件之一：
  1）如果S中所有Acceptor都没有接受过议案的话，那么Pnew的编号保证唯一性和递增即可，Pnew的值可以是任意值。
  2）如果S中有一个或多个Acceptor曾经接受过议案的话，要先找出其中编号最大的那个议案，假设它的编号为N，值为V。那么Pnew的编号必须大于N，Pnew的值必须等于V。**