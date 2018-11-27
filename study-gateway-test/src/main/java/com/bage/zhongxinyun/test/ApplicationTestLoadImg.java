package com.bage.zhongxinyun.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationTestLoadImg implements CommandLineRunner {

    public void run(String... args) throws Exception {
        System.out.println("... get Imgs ...");
        List<Object[]> list = new ArrayList<Object[]>();
        
        initData(list);
        
        downloadImg(list);

    }

	private void initData(List<Object[]> list) {
		list.add(new Object[] {"3632"});
        list.add(new Object[] {"3376"});
        list.add(new Object[] {"3635"});
        list.add(new Object[] {"8932"});
        list.add(new Object[] {"3875"});
        list.add(new Object[] {"8472"});
        list.add(new Object[] {"4696"});
        list.add(new Object[] {"9931"});
        list.add(new Object[] {"3383"});
        list.add(new Object[] {"4709"});
        list.add(new Object[] {"3638"});
        list.add(new Object[] {"9102"});
        list.add(new Object[] {"9292"});
        list.add(new Object[] {"9707"});
        list.add(new Object[] {"9477"});
        list.add(new Object[] {"4852"});
        list.add(new Object[] {"9105"});
        list.add(new Object[] {"4699"});
        list.add(new Object[] {"4858"});
        list.add(new Object[] {"10400"});
	}

	private void downloadImg(final List<Object[]> list) {
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		for (int i = 0; i < list.size(); i++) {
			final int index = i;
			executorService.execute(new Runnable() {
				
				public void run() {
					long bf =  System.currentTimeMillis();
					Object docId = list.get(index)[0];
					HttpClients.get("http://test-zcn.c.citic/Liems/ShowFileContent?webserviceDownload=true&docId=" + docId, String.valueOf(docId));
			        System.out.println(docId + "-下载耗时-" + (System.currentTimeMillis() - bf));
				}
			});
		}
	}

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTestLoadImg.class, args);
        
    }

}