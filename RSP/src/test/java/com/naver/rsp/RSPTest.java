package com.naver.rsp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.file.Path;

@Slf4j
public class RSPTest {

    @Test
    public void test() {
        Path path = RSP.run("images/lenna.png");

        Path path1 = RSP.run("images/test1.jpg");

        Path path2 = RSP.run("images/test2.jpg");

        Path path3 = RSP.run("images/test3.jpg");
    }

}
