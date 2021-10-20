package com.fungisearch.fudriver.reclassification.scheduler;

import com.fungisearch.fudriver.config.AppConfig;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.reclassification.command.service.ReclassificationCommandService;
import com.fungisearch.fudriver.reclassification.query.dao.SkupRodzajDao;
import com.fungisearch.fudriver.reclassification.query.dto.SkupRodzajDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by marcin on 02.02.16.
 */
@Component
@Transactional
public class ScheduledTypesSynchronization {

    private final SkupRodzajDao skupRodzajDao;
    private final ReclassificationCommandService reclassificationCommandService;
    private final AppConfig appConfig;
    private final RodzajSkupFactory rodzajSkupFactory;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public ScheduledTypesSynchronization(SkupRodzajDao skupRodzajDao, ReclassificationCommandService reclassificationCommandService, AppConfig appConfig, RodzajSkupFactory rodzajSkupFactory) {
        this.skupRodzajDao = skupRodzajDao;
        this.reclassificationCommandService = reclassificationCommandService;
        this.appConfig = appConfig;
        this.rodzajSkupFactory = rodzajSkupFactory;
    }


    @Scheduled(fixedDelay = 50000)
    @Async
    public void synchronizeTypes() {
        importTypes();
    }


    private void importTypes() {
        RemoteDataImporter remoteTypesImporter = new RemoteDataImporter(appConfig.getSkupUrl(), "/rest/type", appConfig.getSkupUsername(), appConfig.getSkupPassword());
        String typesJson = remoteTypesImporter.getRemoteTypes();
        List<SkupRodzajDto> typesOnSkup = convertTypesJsonToList(typesJson);
        List<SkupRodzajDto> localTypes = skupRodzajDao.getAllTypes();
        List<SkupRodzajDto> typesToBeCreated = new ArrayList<SkupRodzajDto>();
        List<SkupRodzajDto> typesToBeUpdated = new ArrayList<SkupRodzajDto>();
        boolean createNewRodzaj = true;
        for (SkupRodzajDto typeOnSkup : typesOnSkup) {
            createRodzajSkupGroup(typeOnSkup);
            createNewRodzaj = true;
            for (SkupRodzajDto localType : localTypes) {
                if (localType.remoteId.equals(typeOnSkup.id)) {
                    if (!String.valueOf(localType.name).equals(String.valueOf(typeOnSkup.name))
                            || !localType.typeGroupId.equals(typeOnSkup.typeGroupId)
                            || !String.valueOf(localType.typeGroupName).equals(String.valueOf(typeOnSkup.typeGroupName))
                            || !String.valueOf(localType.active).equals(String.valueOf(typeOnSkup.active))) {
                        localType.name = typeOnSkup.name;
                        localType.typeGroupId = typeOnSkup.typeGroupId;
                        localType.typeGroupName = typeOnSkup.typeGroupName;
                        localType.active = typeOnSkup.active;
                        typesToBeUpdated.add(localType);
                    }
                    createNewRodzaj = false;
                    break;
                }
            }
            if (createNewRodzaj) {
                SkupRodzajDto localType = new SkupRodzajDto();
                localType.remoteId = typeOnSkup.id;
                localType.name = typeOnSkup.name;
                localType.weight = typeOnSkup.weight;
                localType.description = typeOnSkup.description;
                localType.typeGroupId = typeOnSkup.typeGroupId;
                localType.typeGroupName = typeOnSkup.typeGroupName;
                localType.active = typeOnSkup.active;
                typesToBeCreated.add(localType);
            }
        }
        reclassificationCommandService.saveRodzajeSkup(typesToBeCreated);
        reclassificationCommandService.updateRodzajeSkup(typesToBeUpdated);
        reclassificationCommandService.removeNonExistingTypes(typesOnSkup);
    }

    private void createRodzajSkupGroup(SkupRodzajDto dto) {
        rodzajSkupFactory.getRodzajSkupGrupaBuilder()
                .groupId(dto.typeGroupId)
                .groupName(dto.typeGroupName)
                .build();
    }

    private List<SkupRodzajDto> convertTypesJsonToList(String json) {
        Gson gson = new Gson();
        //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type collectionType = new TypeToken<Collection<SkupRodzajDto>>() {
        }.getType();
        List<SkupRodzajDto> types = gson.fromJson(json, collectionType);
        return types;
    }

}
