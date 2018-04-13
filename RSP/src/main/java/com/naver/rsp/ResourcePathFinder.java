package com.naver.rsp;

import lombok.SneakyThrows;

public final class ResourcePathFinder {

    @SneakyThrows
    public String getClassPathResource(String path) {
        return ClassLoader.getSystemResource(path).getPath();
    }
}
