package myapp.ioc;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service("nullLogger")
public class NullLogger implements ILogger {
    @PostConstruct
    public void start(){
        System.err.printf("Start %s\n", this);
    }

    @PreDestroy
    public void stop(){
        System.err.printf("Stop %s\n", this);
    }
}
