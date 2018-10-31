package com.bage.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

@Controller
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
	
		ContextLoader cc;
		// org.springframework.cache.ehcache.EhCacheCacheManager ehCacheCacheManager;
		System.out.println("index");
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	@RequestMapping("/get")
	@ResponseBody
	public String get(@RequestParam(value="isbn",required=false,defaultValue="0") String isbn) {
		if("0".equals(isbn)) {
			isbn = String.valueOf(new Random().nextInt(1000));
		}
		System.out.println("isbn:" + isbn);
		return bookRepository.get(isbn).toString();
	}
	
	@RequestMapping("/getNoKey")
	@ResponseBody
	public String getNoKey(@RequestParam(value="isbn",required=false,defaultValue="0") String isbn) {
		if("0".equals(isbn)) {
			isbn = String.valueOf(new Random().nextInt(1000));
		}
		System.out.println("isbn:" + isbn);
		return bookRepository.getNoKey(isbn).toString();
	}
	
	@RequestMapping("/cacheEvict")
	@ResponseBody
	public String cacheEvict() {
		System.out.println("cacheEvict");
		bookRepository.cacheEvict();
		return "true";
	}

	@RequestMapping("/testApi")
	@ResponseBody
	public String testApi() {
		System.out.println("cacheManager::" + cacheManager);
		System.out.println("books::" + cacheManager.getCache("books"));
		return "true";
	}
}
