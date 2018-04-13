package com.naver.rsp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;

@Slf4j
public class RSPTest {

    @Test
    public void test() {
        File res0 = RSP.run("images/lenna.png");

        File res1 = RSP.run("images/test1.jpg");

        File res2 = RSP.run("images/test2.jpg");

        File res3 = RSP.run("images/test3.jpg");

        //resFile.delete();
        //res1.delete();
        //res2.delete();
        //res3.delete();
    }

}
