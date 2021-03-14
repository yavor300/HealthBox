package project.healthbox.service.impl;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;
import project.healthbox.events.log.formatter.LogFormatter;
import project.healthbox.service.LoggerService;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LoggerServiceImpl implements LoggerService {

    @Override
    public void log(ApplicationEvent applicationEvent) throws IOException {
        Logger logger = Logger.getLogger(applicationEvent.getClass().getName());
        logger.setLevel(Level.FINE);
        logger.addHandler(new FileHandler());
        try {
            Handler fileHandler = new FileHandler("src/main/java/project/healthbox/events/log/files/ApplicationLog.log", true);
            fileHandler.setFormatter(new LogFormatter());
            logger.addHandler(fileHandler);
            logger.log(Level.INFO, applicationEvent.toString());
            fileHandler.close();
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}