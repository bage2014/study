package com.bage.spring.boot.valid;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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