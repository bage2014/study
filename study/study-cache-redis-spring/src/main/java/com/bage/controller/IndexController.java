package com.bage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class IndexController {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CacheManager cacheManager;

	public void query(String... args) throws Exception {
		System.out.println(".... Fetching books");
		System.out.println("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
		System.out.println("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
		System.out.println("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
		System.out.println("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
		System.out.println("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
		System.out.println("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
	}

	@RequestMapping("/index")
	public String index() {
	
		// org.springframework.cache.ehcache.EhCacheCacheManager ehCacheCacheManager;
		System.out.println("index");
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	@RequestMapping("/cacheTest")
	public String cacheTest() {
		
		// org.springframework.cache.ehcache.EhCacheCacheManager ehCacheCacheManager;
		System.out.println("index");
		cacheManager.getCache("bage").put("bage",new Book("gd","bfafg郭德纲"));
		
		Book bokk = (Book) cacheManager.getCache("bage").get("bage").get();

		System.out.println(bokk);
		return "index";
	}

}
