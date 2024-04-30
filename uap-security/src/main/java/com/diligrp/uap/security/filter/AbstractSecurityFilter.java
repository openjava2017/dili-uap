package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.core.SecurityContextAware;
import org.springframework.web.filter.GenericFilterBean;

public abstract class AbstractSecurityFilter extends GenericFilterBean implements SecurityFilter {
    public void configure(SecurityContext context) {
        if (this instanceof SecurityContextAware) {
            ((SecurityContextAware)this).setSecurityContext(context);
        }

        context.autowireBean(this);
    }
}
