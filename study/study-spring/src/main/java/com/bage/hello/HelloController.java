package com.bage.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/hello")
public class HelloController {

	@RequestMapping("/sayHello")
	public String sayHello(@RequestParam(value="text") String text){
		System.out.println(text);
		return "index";
	}
	
}
