public class Test {  
  
    public static void main(String[] args) {  
        new Thread() {  
            @Override  
            public void run() {  
                Test test = new Test();  
                while (true) {  
                    try {  
                        sleep(5000);  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                    test.printHello();  
                }  
            }  
        }.start();  
    }  
  
    protected void printHello() {  
        System.out.println("hello");  
  
    }  
  
}  