package myapp.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("simpleCalculator")
public class SimpleCalculator implements ICalculator {

    @Autowired
    @Qualifier("nullLogger")
    private ILogger logger;

    @Override
    public int add(int a, int b) {
        int result = a+b;
        logger.log("add(a,b) = "+result);
        return a+b;
    }
}