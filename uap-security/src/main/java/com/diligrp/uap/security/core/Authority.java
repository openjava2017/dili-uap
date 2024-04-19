package com.diligrp.uap.security.core;

public interface Authority {
    Resource getResource();

    int getPermission();
}
