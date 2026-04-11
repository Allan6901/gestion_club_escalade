package myapp;

import myapp.ioc.ILogger;
import myapp.ioc.StdoutLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("prod")
public class TestStdoutLogger {

    @Autowired
    ILogger logger;

    @Test
    public void testStdoutLogger(){
        assertTrue(logger instanceof StdoutLogger);
        logger.log("Hello World");

    }


}
