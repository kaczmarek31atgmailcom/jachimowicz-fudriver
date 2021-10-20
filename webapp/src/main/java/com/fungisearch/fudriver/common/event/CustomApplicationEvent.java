package com.fungisearch.fudriver.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by marcin on 21.12.16.
 */
public class CustomApplicationEvent extends ApplicationEvent {

    private Long id;
    private String message;

    public CustomApplicationEvent(Object source, Long id){
        super(source);
        this.id = id;
    }

    public CustomApplicationEvent(Object source, String message){
        super(source);
        this.message = message;
    }

    public Long getId(){
        return this.id;
    }

    public String getMessage() {
        return message;
    }
}
