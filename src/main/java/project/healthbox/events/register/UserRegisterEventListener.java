package project.healthbox.events.register;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.healthbox.events.log.formatter.LogFormatter;

import java.io.IOException;
import java.util.logging.*;

@Component
public class UserRegisterEventListener {
    private final Logger logger = Logger.getLogger(UserRegisterEventListener.class.getName());

    @EventListener(UserRegisterEvent.class)
    public void onUserRegisterEvent(UserRegisterEvent userRegisterEvent) throws IOException {
        logger.setLevel(Level.FINE);
        logger.addHandler(new FileHandler());
        try {
            Handler fileHandler = new FileHandler("src/main/java/project/healthbox/events/log/files/ApplicationLog.log", true);
            fileHandler.setFormatter(new LogFormatter());
            logger.addHandler(fileHandler);
            logger.log(Level.INFO, userRegisterEvent.toString());
            fileHandler.close();
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}