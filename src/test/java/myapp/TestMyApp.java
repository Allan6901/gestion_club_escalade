package myapp;

import jakarta.annotation.Resource;

import myapp.ioc.HelloService;
import myapp.ioc.IHello;
import myapp.ioc.ILogger;
import myapp.ioc.StderrLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Spring services
 */
@SpringBootTest
@ActiveProfiles("devel")
public class TestMyApp {

	@Autowired
	@Qualifier("fileLoggerWithConstructor")
    ILogger fileLoggerWithConstructor;

	@Autowired
	@Qualifier("beanFileLogger")
	ILogger beanFileLogger;

	@Autowired
	ILogger logger;

	@Autowired
	ApplicationContext context;

	@Autowired
    IHello helloByService;

	@Resource(name = "helloService")
	IHello helloByName;
	
	@Autowired
	String bye;

	@Test

	public void testBeanFileLogger(){
		assertInstanceOf(ILogger.class, beanFileLogger);
		beanFileLogger.log("test1");
	}

	@Test
	public void testFileLoggerWithConstructor(){
        assertInstanceOf(ILogger.class, fileLoggerWithConstructor);
		fileLoggerWithConstructor.log("test");
	}

	@Test
	public void testHelloService() {
		assertTrue(helloByService instanceof HelloService);
		helloByService.hello();
	}

	@Test
	public void testHelloByName() {
		assertEquals(helloByService, helloByName);
	}

	@Test
	public void testHelloByContext() {
		assertEquals(helloByName, context.getBean(IHello.class));
		assertEquals(helloByName, context.getBean("helloService"));
	}

	@Test
	public void testBye() {
		assertEquals(bye, "Bye.");
	}

	@Test
	public void testLogger(){
		assertTrue(logger instanceof StderrLogger);
		logger.log("Hello World");

	}

}

