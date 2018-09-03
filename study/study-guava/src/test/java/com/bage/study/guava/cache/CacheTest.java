package com.bage.study.guava.cache;

import java.security.Key;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.graph.Graph;

public class CacheTest {

	public void testCache() {
		
		
		LoadingCache<String, User> graphs = CacheBuilder.newBuilder()
			       .maximumSize(1000)
			       .expireAfterWrite(10, TimeUnit.SECONDS)
			       //.removalListener(MY_LISTENER)
			       .build(
			           new CacheLoader<String, User>() {
			             public User load(String key){
			               return createExpensiveGraph(key);
			             }
			           });
		
	}
	class User{
		private String id;
		private String name;
		public User(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}
		
	}
}
 
