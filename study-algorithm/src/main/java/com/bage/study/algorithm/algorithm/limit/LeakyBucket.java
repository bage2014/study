package com.bage.study.algorithm.algorithm.limit;

/**
 * https://cloud.tencent.com/developer/article/1838833
 *
 */
public class LeakyBucket {
  public long timeStamp = System.currentTimeMillis();  // 当前时间
  public long capacity; // 桶的容量
  public long rate; // 水漏出的速度
  public long water; // 当前水量(当前累积请求数)

  public boolean grant() {
    long now = System.currentTimeMillis();
    // 先执行漏水，计算剩余水量
    water = Math.max(0, water - (now - timeStamp) * rate); 

    timeStamp = now;
    if ((water + 1) < capacity) {
      // 尝试加水,并且水还未满
      water += 1;
      return true;
    } else {
      // 水满，拒绝加水
      return false;
    }
  }
}