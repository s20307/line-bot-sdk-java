package com.naver.rsp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

@Slf4j
public class FooTest {

    @Test
    public void test() {
        String input = "abc/def.xml";
        log.debug("getBaseName: {}", FilenameUtils.getBaseName(input));
        log.debug("getExtension: {}", FilenameUtils.getExtension(input));
        log.debug("getFullPath: {}", FilenameUtils.getFullPath(input));
        log.debug("getFullPathNoEndSeparator: {}", FilenameUtils.getFullPathNoEndSeparator(input));
        log.debug("getPath: {}", FilenameUtils.getPath(input));
        log.debug("getPrefix: {}", FilenameUtils.getPrefix(input));
        log.debug("getName: {}", FilenameUtils.getName(input));
        log.debug("getPathNoEndSeparator: {}", FilenameUtils.getPathNoEndSeparator(input));
    }
}
