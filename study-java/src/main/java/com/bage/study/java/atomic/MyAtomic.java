package com.bage.study.java.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicIntegerArray 传递数组参数，不更改原始数组
 * @author bage
 *
 */
public class MyAtomic {

	static int n = 10;
	static AtomicInteger ai = new AtomicInteger(n);
	
	static User user = new User("","zhangsan");
	static User newUser = null;
	
	public static void main(String[] args) {
		
		
		// integer();
		// array();
		reference();
		// TODO
		
	}
	private static void reference() {
		AtomicReference<User> userRef = new AtomicReference<User>(user );
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					newUser = userRef.get();
					newUser.setId(newUser.getId() + ",");
					userRef.compareAndSet(user, newUser);
					System.out.println(userRef.get().getId().length());
				}
			});
		}
		executorService.shutdown();
	}
	private static void array() {
		int[] value = new int[]{1,2};
		AtomicIntegerArray ai = new AtomicIntegerArray(value);

        ai.getAndSet(0, 3);
        System.out.println(value[0]);
        System.out.println(ai.get(0));
	}
	private static void integer() {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 100; i++) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					System.out.println(incrementAndGet());
					// System.out.println(ai.incrementAndGet());
				}
			});
		}
		executorService.shutdown();
	}
	private static int incrementAndGet() {
		return n ++;
	}
}
class User{
	private String id;
	private String name;
	public User() {
		super();
	}
	public User(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}
}
