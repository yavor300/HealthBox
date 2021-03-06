package project.healthbox.service.impl;

import org.springframework.stereotype.Service;
import project.healthbox.log.formatter.LogFormatter;
import project.healthbox.service.LoggerService;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LoggerServiceImpl implements LoggerService {

    @Override
    public void log(Object object) throws IOException {
        Logger logger = Logger.getLogger(object.getClass().getName());
        logger.setLevel(Level.FINE);
        logger.addHandler(new FileHandler());
        try {
            Handler fileHandler = new FileHandler("src/main/java/project/healthbox/log/files/Registration.log", true);
            fileHandler.setFormatter(new LogFormatter());
            logger.addHandler(fileHandler);
            logger.log(Level.INFO, object.toString());
            fileHandler.close();
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}