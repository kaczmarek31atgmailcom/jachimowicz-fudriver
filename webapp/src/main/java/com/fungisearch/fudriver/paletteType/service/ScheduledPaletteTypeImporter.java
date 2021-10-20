package com.fungisearch.fudriver.paletteType.service;

import com.fungisearch.fudriver.config.AppConfig;
import com.fungisearch.fudriver.paletteType.command.model.PaletteTypeFactory;
import com.fungisearch.fudriver.paletteType.query.dto.PaletteTypeProxyDto;
import com.fungisearch.fudriver.reclassification.scheduler.RemoteDataImporter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

@Component
@Transactional
public class ScheduledPaletteTypeImporter {
    private final AppConfig appConfig;
    private final PaletteTypeFactory paletteTypeFactory;

    public ScheduledPaletteTypeImporter(AppConfig appConfig, PaletteTypeFactory paletteTypeFactory) {
        this.appConfig = appConfig;
        this.paletteTypeFactory = paletteTypeFactory;
    }


    @Scheduled(fixedDelay = 50000)
    @Async
    public void syncPalettes(){
        RemoteDataImporter remoteTypesImporter = new RemoteDataImporter(appConfig.getSkupUrl(), "/rest/palette", appConfig.getSkupUsername(), appConfig.getSkupPassword());
        String palettesJson = remoteTypesImporter.getRemoteTypes();
        List<PaletteTypeProxyDto> palettes = convertTypesJsonToList(palettesJson);
        palettes.forEach(p -> paletteTypeFactory
        .getBuilder()
        .remotePaletteId(p.paletteId)
        .name(p.name)
        .active(p.active)
        .build());
    }


    private List<PaletteTypeProxyDto> convertTypesJsonToList(String json) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<PaletteTypeProxyDto>>() {
        }.getType();
        List<PaletteTypeProxyDto> palettes = gson.fromJson(json, collectionType);
        return palettes;
    }
}
