package com.bage.study.java.multhread;

public class WaitNotify {

    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify();
        waitNotify.test();
    }

    private void test() {
        ShareObj shareObj = new ShareObj();


        Thread11 thread11 = new Thread11(shareObj);
        thread11.start();
        System.out.println("线程11启动。。。");


        Thread1 thread1 = new Thread1(shareObj);
        thread1.start();
        System.out.println("线程1启动。。。");


        Thread2 thread2 = new Thread2(shareObj);
        thread2.start();
        System.out.println("线程2启动。。。");
    }

    class Thread2 extends Thread{
        ShareObj shareObj = null;
        public Thread2(ShareObj shareObj){
            this.shareObj = shareObj;
        }

        @Override
        public void run() {
            try {
                System.out.println("线程2开始执行XXXX逻辑");
                Thread.sleep(5000);
                System.out.println("线程2执行XXXX逻辑结束");
                synchronized (shareObj){
                    shareObj.notifyAll();
                    System.out.println("线程2执行notify方法");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Thread1 extends Thread{
        ShareObj shareObj = null;
        public Thread1(ShareObj shareObj){
            this.shareObj = shareObj;
        }

        @Override
        public void run() {
            synchronized (shareObj){
                System.out.println("线程1加锁。。。");
                if(shareObj.isNotOk()){
                    try {
                        System.out.println("线程1正在等待。。。");
                        shareObj.wait();
                        System.out.println("线程1等待结束。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }


    class Thread11 extends Thread{
        ShareObj shareObj = null;
        public Thread11(ShareObj shareObj){
            this.shareObj = shareObj;
        }

        @Override
        public void run() {
            synchronized (shareObj){
                System.out.println("线程11加锁。。。");
                if(shareObj.isNotOk()){
                    try {
                        System.out.println("线程11正在等待。。。");
                        shareObj.wait();
                        System.out.println("线程11等待结束。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    class ShareObj{
        private Object data = null;

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public boolean isNotOk(){
            return getData() == null;
        }

    }


}
