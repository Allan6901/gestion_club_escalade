package myapp;

import myapp.ioc.CalculatorWithLog;
import myapp.ioc.ICalculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("devel")
public class TestCalculator {

    @Autowired
    @Qualifier("calculatorWithLog")
    ICalculator calculator;

    @Test
    public void testCalculatorWithLog(){
        var res = calculator.add(10,20);
        assertEquals(30,res);
        assertTrue(calculator instanceof CalculatorWithLog);
    }

}
