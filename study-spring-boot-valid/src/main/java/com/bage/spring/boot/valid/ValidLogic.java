package com.bage.spring.boot.valid;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.*;
import java.util.Set;


@Component
public class ValidLogic {

	public String hello(PersonForm personForm) {
		//引入校验工具
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		//获取校验器
		Validator validator = factory.getValidator();
		//执行校验
		Set<ConstraintViolation<PersonForm>> violationSet = validator.validate(personForm);
		violationSet.forEach(violat -> {
			System.out.println(violat.getPropertyPath());//校验错误的域
			System.out.println(violat.getMessage());//校验错误的信息
		});
		return "form";
	}

}