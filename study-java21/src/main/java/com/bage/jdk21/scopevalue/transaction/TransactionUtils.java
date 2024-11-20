package com.bage.jdk21.scopevalue.transaction;

import java.util.List;

public class TransactionUtils {
    public static int doSubmitDb(List<Runnable> runnableList) {
        for (int i = 0; i < runnableList.size(); i++) {
            runnableList.get(i).run();
        }
        return 1;
    }
}
