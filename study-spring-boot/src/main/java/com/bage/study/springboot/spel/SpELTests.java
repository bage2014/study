package com.bage.study.springboot.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

/**
 * https://docs.spring.io/spring-framework/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html
 */
public class SpELTests {

    public static void main(String[] args) {
        exp(); // 常量、用单引号括起来
        javaMethod(); // java 方法
        objectField(); // java 对象属性解析
        updateObjectField(); // 更新java 对象属性
        config(); // 配置
        operation(); // 运算 或者 Logical operators
        variables(); // 特殊变量

    }

    private static void variables() {
// create an array of integers
        List<Integer> primes = new ArrayList<Integer>();
        primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

// create parser and set variable 'primes' as the array of integers
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("primes",primes);

// all prime numbers > 10 from the list (using selection ?{...})
// evaluates to [11, 13, 17]
        List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression(
                "#primes.?[#this>10]").getValue(context);
        System.out.println("variables:" + primesGreaterThanTen);
    }

    private static void operation() {
        ExpressionParser parser = new SpelExpressionParser();
        boolean trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);
        System.out.println("operation:" + trueValue);
        // evaluates to true
        trueValue = parser.parseExpression("true or false").getValue(Boolean.class);
        System.out.println("operation:" + trueValue);
        String falseString = parser.parseExpression(
                "false ? 'trueExp' : 'falseExp'").getValue(String.class);
        System.out.println("operation:" + falseString);
    }

    private static void config() {
// Turn on:
// - auto null reference initialization
// - auto collection growing
        SpelParserConfiguration config = new SpelParserConfiguration(true,true);

        ExpressionParser parser = new SpelExpressionParser(config);

        Expression expression = parser.parseExpression("list[3]");

        Demo demo = new Demo();

        Object o = expression.getValue(demo);

// demo.list will now be a real collection of 4 entries
// Each entry is a new empty String
        System.out.println("config:" + demo.list.size());

    }

    private static void updateObjectField() {
        // Create and set a calendar
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

// The constructor arguments are name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");

        EvaluationContext context = new StandardEvaluationContext(tesla);
        exp.setValue(context,"new-Name");
        System.out.println("objectField:" + tesla.getName());
    }

    private static void objectField() {

        // Create and set a calendar
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

// The constructor arguments are name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");

        EvaluationContext context = new StandardEvaluationContext(tesla);
        String name = (String) exp.getValue(context);
        System.out.println("objectField:" + name);

    }

    private static void javaMethod() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");
        String message = exp.getValue(String.class);
        System.out.println("javaMethod:" + message);
    }

    private static void exp() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue();
        System.out.println("exp:" + message);
    }

    static class Inventor {
        private String name;
        private Date birthday;
        private String nationality;

        public Inventor(String name, Date birthday, String nationality) {
            this.name = name;
            this.birthday = birthday;
            this.nationality = nationality;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }
    }
    static class Demo {
        public List<String> list;

    }
}
