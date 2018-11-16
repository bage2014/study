package com.bage.study.materialize.spring.boot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/starter")
public class StarterController {
	
	@RequestMapping("/{templates}")
	public String getUser(@PathVariable String templates) {
		return "starter/" + templates;
	}
	
}

