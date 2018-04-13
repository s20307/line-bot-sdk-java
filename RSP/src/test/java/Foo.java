import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

@Slf4j
public class Foo {

    @SneakyThrows
    @Test
    public void test() {
        //getClass().getClassLoader()
        //String str = IOUtils.resourceToString("lbpcascades/lbpcascade_frontalface.xml", StandardCharsets.UTF_8);

        String path = this.getClass().getResource("lbpcascades/lbpcascade_frontalface.xml").toURI().getPath();


        String contents =
                FileUtils.readFileToString(
                        new File(this.getClass().getResource("lbpcascades/lbpcascade_frontalface.xml").toURI()));

        log.debug(path);
        //log.debug(contents);
    }
}
