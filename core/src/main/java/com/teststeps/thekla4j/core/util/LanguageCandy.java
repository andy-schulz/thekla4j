package com.teststeps.thekla4j.core.util;

public abstract class LanguageCandy<T extends LanguageCandy<T>> {
    @SuppressWarnings("unchecked")
    public T and = (T) this,
            with = (T) this,
            using = (T) this;
}
