package timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TimerTask、Timer的最简单使用
 * @author luruihua
 *
 */
public class MyTimer extends TimerTask{
	
    public static void main(String[] args) {
    	
        //创建定时器对象
        Timer timer = new Timer();
        //在3秒后执行MyTask类中的run方法,后面每10秒跑一次
        timer.schedule(new MyTimer(), 2000,1000);
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("MyTimer is running: " + new Date().toString());
	}
}
