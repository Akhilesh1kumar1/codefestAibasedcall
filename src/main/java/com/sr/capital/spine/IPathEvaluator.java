package com.sr.capital.spine;

public interface IPathEvaluator {
    public Object evaluate(Object template, Object seedData);
    public <T> T evaluate(Object template, Object seedData, Class<T> valueType);
}

