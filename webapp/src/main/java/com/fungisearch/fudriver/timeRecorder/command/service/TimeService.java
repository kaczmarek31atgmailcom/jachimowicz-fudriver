package com.fungisearch.fudriver.timeRecorder.command.service;

import com.fungisearch.fudriver.common.event.CustomApplicationEvent;
import com.fungisearch.fudriver.common.event.EventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TimeService{
    public final ApplicationEventPublisher applicationEventPublisher;


    @Autowired
    public TimeService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


/*
    LocalDateTime dateTime = LocalDateTime.now();

    @Scheduled(fixedDelay = 1000)
    public void getCurrentTime(){
        LocalDateTime now = LocalDateTime.now();
        dateTime = now;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.TIME_MINUTE_CHANGED,dateTime.format(formatter));
            applicationEventPublisher.publishEvent(event);
    }
*/
}
