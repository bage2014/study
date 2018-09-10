package com.bage.ehcache.tutorial;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class JavaCache {

	public static void tutorial() {
		CacheManager cacheManager = null;
		try {
			cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
					.withCache("preConfigured", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
							String.class, ResourcePoolsBuilder.heap(10)))
					.build();
			cacheManager.init();

			Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);

			System.out.println("preConfigured:" + preConfigured);
			
			Cache<Long, String> myCache = cacheManager.createCache("myCache", CacheConfigurationBuilder
					.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)));

			myCache.put(1L, "da one!");
			String value = myCache.get(1L);
			System.out.println("value:" + value);

			cacheManager.removeCache("preConfigured");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cacheManager != null) {
				cacheManager.close();
			}
		}
	}
	
}
