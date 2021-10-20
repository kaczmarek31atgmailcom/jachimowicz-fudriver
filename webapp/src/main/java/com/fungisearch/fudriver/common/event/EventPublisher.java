package com.fungisearch.fudriver.common.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

/**
 * Created by marcin on 21.12.16.
 */
@Controller
public class EventPublisher implements ApplicationListener<CustomApplicationEvent> {

    private static Long key = 1L;
    private static Map<Long,SseEmitter> sseEmitters = new ConcurrentHashMap<Long, SseEmitter>();

    @Override
    public void onApplicationEvent(CustomApplicationEvent event) {
        sseEmitters.forEach((k, v) -> {
            try {
                v.send(event, APPLICATION_JSON_UTF8);
            } catch (IOException e) {
                v.complete();
                sseEmitters.remove(k);
            }
            catch(IllegalStateException e){
                sseEmitters.remove(k);
            }
        });
    }

    @RequestMapping(value = "/events/connect", method = RequestMethod.GET)
    public SseEmitter openConnection() {
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitters.put(key++,sseEmitter);
        return sseEmitter;
    }

}
