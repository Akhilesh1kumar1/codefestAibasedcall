package com.sr.capital.spine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.expression.*;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Slf4j
@Component
public class SpelEvalualator {

    private final ExpressionParser parser = new SpelExpressionParser();

    private static final String PREFIX = "${";
    private static final String SUFFIX = "}";

    public Map<String, Object> bindVariableInternal(Map<String, Object> aMap, Object aContext) {
        Map<String, Object> newMap = new HashMap<String, Object>();
        for (Entry<String, Object> entry : aMap.entrySet()) {
            newMap.put(entry.getKey(), bindVariables(entry.getValue(), aContext));
        }
        return newMap;
    }

    @SuppressWarnings("unchecked")
    public Object bindVariables(Object aValue, Object aContext) {
        StandardEvaluationContext context = createEvaluationContext(aContext);
        if (aValue instanceof String) {
            Expression expression = parser.parseExpression((String) aValue, new TemplateParserContext(PREFIX, SUFFIX));
            try {
                return (expression.getValue(context));
            } catch (SpelEvaluationException e) {
                log.debug(e.getMessage());
                return aValue;
            }
        } else if (aValue instanceof List) {
            List<Object> evaluatedlist = new ArrayList<>();
            List<Object> list = (List<Object>) aValue;
            for (Object item : list) {
                evaluatedlist.add(bindVariables(item, aContext));
            }
            return evaluatedlist;
        } else if (aValue instanceof Map) {
            return bindVariableInternal((Map<String, Object>) aValue, aContext);
        }
        return aValue;
    }

    public Object evaluate(Object aValue, Object aContext) {
        if (aValue instanceof String) {
            String expression = (String) this.bindVariables(aValue, aContext);
            try {
                return parser.parseExpression(expression).getValue();
            } catch (NullPointerException | SpelEvaluationException e) {
                log.debug(e.getMessage());
                return aValue;
            }
        } else if (aValue instanceof List) {
            return this.evaluateCollection(aValue, aContext);
        } else if (aValue instanceof Map) {
            return this.evaluateMap(aValue, aContext);
        }

        return aValue;
    }

    public List<Object> evaluateCollection(Object aValue, Object aContext) {
        List<Object> evaluatedlist = new ArrayList<>();
        List<Object> list = (List<Object>) aValue;
        for (Object item : list) {
            String expression = (String) this.bindVariables(item, aContext);
            evaluatedlist.add(parser.parseExpression(expression).getValue());
        }
        return evaluatedlist;
    }

    public Map<String, Object> evaluateMap(Object aValue, Object aContext) {
        Map<String, Object> expressionEvaluatedMap = new HashMap<>();
        Map<String, Object> bindedExpression = this.bindVariableInternal((Map<String, Object>) aValue, aContext);
        bindedExpression.forEach((key, expression) -> {
            expressionEvaluatedMap.put(key, parser.parseExpression((String) expression).getValue());
        });
        return expressionEvaluatedMap;
    }

    private StandardEvaluationContext createEvaluationContext(Object aContext) {
        StandardEvaluationContext context = new StandardEvaluationContext(aContext);
        context.addPropertyAccessor(new MapPropertyAccessor());
        context.addMethodResolver(methodResolver());
        return context;
    }

    private MethodResolver methodResolver() {
        return (ctx, target, name, args) -> {
            switch (name) {
                case "systemProperty":
                    return this::systemProperty;
                case "range":
                    return range();
                case "boolean":
                    return cast(Boolean.class);
                case "byte":
                    return cast(Byte.class);
                case "char":
                    return cast(Character.class);
                case "short":
                    return cast(Short.class);
                case "int":
                    return cast(Integer.class);
                case "long":
                    return cast(Long.class);
                case "float":
                    return cast(Float.class);
                case "double":
                    return cast(Double.class);
                case "join":
                    return join();
                case "concat":
                    return concat();
                case "flatten":
                    return flatten();
                default:
                    return null;
            }
        };
    }

    private TypedValue systemProperty(EvaluationContext aContext, Object aTarget, Object... aArgs)
            throws AccessException {
        return new TypedValue(System.getProperty((String) aArgs[0]));
    }

    private MethodExecutor range() {
        return (ctx, target, args) -> {
            List<Integer> value = IntStream.rangeClosed((int) args[0], (int) args[1]).boxed()
                    .collect(Collectors.toList());
            return new TypedValue(value);
        };
    }

    private <T> MethodExecutor cast(Class<T> type) {
        return (ctx, target, args) -> {
            T value = type.cast(ConvertUtils.convert(args[0], type));
            return new TypedValue(value);
        };
    }

    @SuppressWarnings("unchecked")
    private <T> MethodExecutor join() {
        return (ctx, target, args) -> {
            String separator = (String) args[0];
            List<T> values = (List<T>) args[1];
            String str = values.stream().map(String::valueOf).collect(Collectors.joining(separator));
            return new TypedValue(str);
        };
    }

    @SuppressWarnings("unchecked")
    private <T> MethodExecutor concat() {
        return (ctx, target, args) -> {
            List<T> l1 = (List<T>) args[0];
            List<T> l2 = (List<T>) args[1];
            List<T> joined = new ArrayList<T>(l1.size() + l2.size());
            joined.addAll(l1);
            joined.addAll(l2);
            return new TypedValue(joined);
        };
    }

    @SuppressWarnings("unchecked")
    private <T> MethodExecutor flatten() {
        return (ctx, target, args) -> {
            List<List<T>> list = (List<List<T>>) args[0];
            List<T> flat = list.stream().flatMap(List::stream).collect(Collectors.toList());
            return new TypedValue(flat);
        };
    }

    public Object getValue(String[] parameterNames, Object[] args, String key) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
