package myapp.ioc;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("calculator")
@Data
public class Calculator implements ICalculator{

    @Qualifier("calculator")
    private ICalculator calc;
    private ILogger logger;

    public Calculator(
            @Qualifier("simpleCalculator") ICalculator calc,
            @Autowired ILogger logger) {
                this.calc = calc;
                this.logger = logger;
    }


    @Override
    public int add(int a, int b) {
        int result = calc.add(a,b);
        logger.log("add("+a+","+b+") = "+result);
        return result;
    }

}
