package com.bage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@Autowired
	private BookRepository bookRepository;

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

}
