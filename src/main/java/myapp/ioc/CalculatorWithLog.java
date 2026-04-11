package myapp.ioc;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.Data;

@Service("calculatorWithLog")
@Data
public class CalculatorWithLog implements ICalculator {

    @Autowired
    @Qualifier("nullLogger")
    private ILogger logger;

    @PostConstruct
    public void start() {
        Assert.notNull(logger, "no logger");
    }

    @Override
    public int add(int a, int b) {
        logger.log(String.format("add(%d,%d)", a, b));
        return (a + b);
    }

}