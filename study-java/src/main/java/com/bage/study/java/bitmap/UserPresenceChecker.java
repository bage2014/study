package com.bage.study.java.bitmap;

import java.util.BitSet;

public class UserPresenceChecker {
    private BitSet userBitSet;

    public static void main(String[] args) {
        long[] userIdList = {123, 234, 567};
        for (Long userId : userIdList) {
            System.out.println("用户 " + userId + (checkUserPresence(userId) ? " 存在" : " 不存在"));
        }
    }


    public UserPresenceChecker() {
        userBitSet = new BitSet();
    }

    public void addUser(long userId) {
        userBitSet.set((int) userId);
    }

    public boolean isUserPresent(long userId) {
        return userBitSet.get((int) userId);
    }

    public static boolean checkUserPresence(long userId) {
        UserPresenceChecker presenceChecker = new UserPresenceChecker();

        // 假设有一亿用户存在
        for (int i = 0; i < 100000000; i++) {
            presenceChecker.addUser(i);
        }

        //模拟123不存在
        presenceChecker.userBitSet.set(123, false);
        return presenceChecker.isUserPresent(userId);
    }

}
