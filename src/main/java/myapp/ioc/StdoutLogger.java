package myapp.ioc;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Primary
@Profile("!devel") // class pour tous les profils sauf devel
public class StdoutLogger implements ILogger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
