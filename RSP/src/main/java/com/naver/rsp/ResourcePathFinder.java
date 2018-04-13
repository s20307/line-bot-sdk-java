package com.naver.rsp;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ResourcePathFinder {

    @SneakyThrows
    public String getClassPathResource(String path) {
        String fileName = FilenameUtils.getBaseName(path);
        String extention = FilenameUtils.getExtension(path);
        Path tempFile = Files.createTempFile(fileName, extention);
        tempFile.toFile().deleteOnExit();

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        FileUtils.copyInputStreamToFile(inputStream, tempFile.toFile());

        return tempFile.toString();
    }
}
