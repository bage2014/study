package com.bage.study.springboot.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * https://docs.spring.io/spring/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html
 */
public class SpelTest {

    public static void main(String[] args) {

        ExpressionParser parser = new SpelExpressionParser();
        Hello hello = new Hello();

        hello.setName("bage");
        EvaluationContext context = new StandardEvaluationContext(hello);

        String name = (String) parser.parseExpression("name").getValue(context);

        System.out.println("result:" + name);


    }

}

class Hello {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
