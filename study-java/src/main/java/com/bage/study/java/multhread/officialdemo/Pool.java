package com.bage.study.java.multhread.officialdemo;

import java.util.concurrent.Semaphore;

public class Pool {
	private static final int MAX_AVAILABLE = 3;
	private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

	public Object getItem() throws InterruptedException {
		available.acquire();
		return getNextAvailableItem();
	}

	public void putItem(char x) {
		if (markAsUnused(x))
			available.release();
	}

	// Not a particularly efficient data structure; just for demo

	protected char[] items = "0123456789abcedfghijklmnopqrstuvwxyzABCEDFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
																    // ...
																	// whatever
																	// kinds of
																	// items
																	// being
																	// managed
	protected boolean[] used = new boolean[MAX_AVAILABLE];

	protected synchronized Object getNextAvailableItem() {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (!used[i]) {
				used[i] = true;
				return items[i];
			}
		}
		return null; // not reached
	}

	protected synchronized boolean markAsUnused(char item) {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (item == items[i]) {
				if (used[i]) {
					used[i] = false;
					return true;
				} else
					return false;
			}
		}
		return false;
	}
}
