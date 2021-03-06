package com.vzoom.apocalypse.common.spel;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class SpelExcetor {

    @Autowired
    private ApplicationContext appContext;

    private final static Map<String, Expression> ELS_TO_EXPS = new ConcurrentHashMap<>();

    private static ExpressionParser parser = new SpelExpressionParser();

    private BeanResolver beanResolver = (context, beanName) -> appContext.getBean(beanName);

    @SuppressWarnings("unchecked")
    public static <O> O done(String targetIdEL) {
        EvaluationContext elContext = newElContext(null);
        return (O) parseExpression(targetIdEL).getValue(elContext);
    }

    @SuppressWarnings("unchecked")
    public static <O> O done(Map<String, Object> argDetail, String targetIdEL) {
        EvaluationContext elContext = newElContext(argDetail);
        return (O) parseExpression(targetIdEL).getValue(elContext);
    }

    @SuppressWarnings("unchecked")
    public <O> O doneInSpringContext(Map<String, Object> argDetail, String targetIdEL) {
        EvaluationContext elContext = newSpringElContext(argDetail);
        return (O) parseExpression(targetIdEL).getValue(elContext);
    }


    @SuppressWarnings("unchecked")
    public <O> O doneInSpringContext(String targetIdEL) {
        EvaluationContext elContext = newSpringElContext(null);
        return (O) parseExpression(targetIdEL).getValue(elContext);
    }

    protected static EvaluationContext newElContext(Map<String, Object> elArgs) {
        StandardEvaluationContext elContext = new StandardEvaluationContext();
        if (elArgs != null && !elArgs.isEmpty()) {
            elContext.setVariables(elArgs);
        }
        return elContext;
    }

    protected EvaluationContext newSpringElContext(Map<String, Object> elArgs) {
        StandardEvaluationContext elContext = new StandardEvaluationContext();
        if (appContext == null) {
            throw new RuntimeException("NOT HAS SPRING CONTEXT");
        }
        elContext.setBeanResolver(beanResolver);
        if (elArgs != null && !elArgs.isEmpty()) {
            elContext.setVariables(elArgs);
        }
        return elContext;
    }

    protected static Expression parseExpression(String el) {
        Expression exp = ELS_TO_EXPS.get(el);
        if (Objects.isNull(exp)) {
            exp = parser.parseExpression(el);
            Expression oldData = ELS_TO_EXPS.putIfAbsent(el, exp);
            if (oldData != null) {
                exp = oldData;
            }
        }
        return exp;
    }

}

