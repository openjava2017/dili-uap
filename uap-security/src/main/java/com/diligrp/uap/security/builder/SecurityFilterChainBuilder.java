package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.filter.WebSecurityFilterChain;
import com.diligrp.uap.security.util.AnyRequestMatcher;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import com.diligrp.uap.security.util.ObjectPostProcessor;
import jakarta.servlet.Filter;

import java.util.*;
import java.util.stream.Collectors;

public class SecurityFilterChainBuilder implements SecurityBuilder<SecurityFilterChain> {

    private final List<OrderedFilter> filters = new ArrayList<>();

    private HttpRequestMatcher requestMatcher = AnyRequestMatcher.INSTANCE;

    private final RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder();

    private final Map<Class<? extends SecurityFilterBuilder>, SecurityFilterBuilder> builders = new HashMap<>();

    private final ObjectPostProcessor<Object> objectPostProcessor;

    public void addFilter(Filter filter, int priority) {
        filters.add(new OrderedFilter(filter, priority));
    }

    public SecurityFilterChainBuilder(ObjectPostProcessor<Object> objectPostProcessor) {
        this.objectPostProcessor = objectPostProcessor;
    }

    public SecurityFilterChainBuilder securityMatcher(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        customizer.customize(requestMatcherBuilder);
        return this;
    }

    public SecurityFilterChainBuilder exceptionHandle(SecurityCustomizer<ExceptionHandleBuilder> customizer) {
        ExceptionHandleBuilder builder = new ExceptionHandleBuilder(objectPostProcessor);
        ExceptionHandleBuilder one = (ExceptionHandleBuilder) this.builders.putIfAbsent(builder.getClass(), builder);
        customizer.customize(one == null ? builder : one);
        return this;
    }

    @Override
    public SecurityFilterChain build() {
        this.requestMatcher = requestMatcherBuilder.build().orElse(this.requestMatcher);

        List<SecurityFilterBuilder> builders = new ArrayList<>(this.builders.values());
        builders.stream().map(builder -> new OrderedFilter(builder.build(), builder.priority()))
            .collect(Collectors.toCollection(() -> this.filters));
        this.filters.sort(Comparator.comparingInt(OrderedFilter::getPriority));

        List<Filter> filters = this.filters.stream().map(f -> f.getFilter()).collect(Collectors.toList());
        return new WebSecurityFilterChain(this.requestMatcher, filters);
    }

    private static class OrderedFilter {

        private Filter filter;

        private int priority;

        public OrderedFilter(Filter filter, int priority) {
            this.filter = filter;
            this.priority = priority;
        }

        public Filter getFilter() {
            return filter;
        }

        public int getPriority() {
            return priority;
        }
    }
}
