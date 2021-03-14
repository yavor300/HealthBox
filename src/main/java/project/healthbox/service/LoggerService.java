package project.healthbox.service;

import org.springframework.context.ApplicationEvent;

import java.io.IOException;

public interface LoggerService<T extends ApplicationEvent> {
    void log(T applicationEvent) throws IOException;
}
