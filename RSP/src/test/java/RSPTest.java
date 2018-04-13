import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;

@Slf4j
public class RSPTest {

    @Test
    public void test() {
        //RSP.run("./images/lenna.png", "./images/output.png");

        File srcFile = new File("images/lenna.png");
        File resFile = RSP.run(srcFile);

        File srcFile1 = new File("images/test1.jpg");
        File res1 = RSP.run(srcFile1);

        File srcFile2 = new File("images/test2.jpg");
        File res2 = RSP.run(srcFile2);

        File srcFile3 = new File("images/test3.jpg");
        File res3 = RSP.run(srcFile3);

        //resFile.delete();
        //res1.delete();
        //res2.delete();
        //res3.delete();
    }

}
