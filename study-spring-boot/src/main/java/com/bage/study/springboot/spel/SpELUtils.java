package com.bage.study.springboot.spel;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class SpELUtils {

    private static ExpressionParser parser = new SpelExpressionParser();
    private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public static EvaluationContext buildEvaluationContext(Method method, Object[] arguments) {
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], arguments[len]);
        }
        return context;
    }

    public static <T> T parseSpELValue(EvaluationContext context, String SpEL, Class<T> clazz, T defaultResult) {
        try {
            Expression expression = parser.parseExpression(SpEL);
            return expression.getValue(context, clazz);
        } catch (Exception e) {

        }
        return defaultResult;
    }

}
