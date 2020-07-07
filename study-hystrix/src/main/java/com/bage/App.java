package com.bage;

import rx.Observable;
import rx.Observer;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        Observable<String> fWorld = new CommandHelloWorld("Bob").observe();

        // - this is a verbose anonymous inner-class approach and doesn't do assertions
        fWorld.subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {
                // nothing needed here
                System.out.println("onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                System.out.println("onNext: " + v);
            }

        });
//        Future<String> s = new CommandHelloWorld("Bob").queue();
//        Observable<String> s = new CommandHelloWorld("Bob").observe();

                String s = new CommandHelloWorld("Bob").execute();
        System.out.println(s);
    }

}
