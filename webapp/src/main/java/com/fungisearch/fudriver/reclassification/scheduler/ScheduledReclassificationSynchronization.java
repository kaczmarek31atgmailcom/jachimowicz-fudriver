package com.fungisearch.fudriver.reclassification.scheduler;

import com.fungisearch.fudriver.config.AppConfig;
import com.fungisearch.fudriver.reclassification.command.model.ReclassificationDetailSkup;
import com.fungisearch.fudriver.reclassification.command.model.ReclassificationSkup;
import com.fungisearch.fudriver.reclassification.command.repository.ReclassificationDetailSkupRepository;
import com.fungisearch.fudriver.reclassification.command.repository.ReclassificationSkupRepository;
import com.fungisearch.fudriver.reclassification.command.service.ReclassificationCommandService;
import com.fungisearch.fudriver.reclassification.query.dao.ReclassificationSkupDao;
import com.fungisearch.fudriver.reclassification.query.dto.RemoteReclassificationDetailDto;
import com.fungisearch.fudriver.reclassification.query.dto.RemoteReclassificationDto;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by marcin on 04.02.16.
 */
@Component
public class ScheduledReclassificationSynchronization {

    @Autowired
    private ReclassificationCommandService reclassificationCommandService;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ReclassificationSkupDao reclassificationSkupDao;


    @Autowired
    private BeanValidator beanValidator;

    @Autowired
    private ReclassificationSkupRepository reclassificationSkupRepository;

    @Autowired
    private ReclassificationDetailSkupRepository reclassificationDetailSkupRepository;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private RemoteReclassificationsImporter importer;

    //@Scheduled(fixedDelay = 10000)
    public void synchronizeReclassifications() {
        String skupUrl = appConfig.getSkupUrl();
        String supplierNr = appConfig.getSupplierNr();
        String username = appConfig.getSkupUsername();
        String password = appConfig.getSkupPassword();
        importer = new RemoteReclassificationsImporter(skupUrl, supplierNr, username, password);

        Set<Long> reclassIdsToBeImported = checkForNewReclassificationIds();
        if (!reclassIdsToBeImported.isEmpty()) {
            importReclassifications(reclassIdsToBeImported);
        }
    }

    private void importReclassifications(Set<Long> reclassificationsToBeImported) {
        for (Long remoteReclassId : reclassificationsToBeImported) {
            RemoteReclassificationDto headerDto = importer.getRemoteReclassification(remoteReclassId);
            List<RemoteReclassificationDetailDto> itemsDto = importer.getRemoteHeaders(remoteReclassId);
            ReclassificationSkup header = reclassificationMapper(headerDto);
            List<ReclassificationDetailSkup> details = reclassificationDetailsMapper(itemsDto,remoteReclassId);

            if(!details.isEmpty()) {
                reclassificationSkupRepository.save(header);
                for(ReclassificationDetailSkup detail: details) {
                    reclassificationDetailSkupRepository.save(detail);
                }
            }
        }
    }

    private ReclassificationSkup reclassificationMapper(RemoteReclassificationDto dto){
        ReclassificationSkup entity = new ReclassificationSkup();
        entity.setRemoteId(dto.id);
        entity.setCreated(dto.created);
        entity.setDescription(dto.description);
        entity.setUserId(new Long(1));
        entity.setProcessed(false);
        beanValidator.validate(entity);
        return entity;
    }

    private List<ReclassificationDetailSkup> reclassificationDetailsMapper(List<RemoteReclassificationDetailDto> dtos, Long reclassificationId){
        Map<Long,ReclassificationDetailSkup> details = new LinkedHashMap<Long,ReclassificationDetailSkup>();
        for(RemoteReclassificationDetailDto dto: dtos){
            ReclassificationDetailSkup entity = null;
            if(details.containsKey(dto.id)){
                entity = details.get(dto.id);
            } else {
                entity = new ReclassificationDetailSkup();
            }
            entity.setReclassificationId(reclassificationId);
            entity.setBarcode(dto.barcode);
            entity.setRemoteRodzajId(dto.rodzajId);
            entity.setPickerId(findPickerIdInBarcode(dto.barcode));
            entity.setUniqId(findUniqIdInBarcode(dto.barcode));
            entity.setActive(true);
            beanValidator.validate(entity);
            details.put(dto.id,entity);
        }
    return new ArrayList(details.values());
    }

    private Set<Long> checkForNewReclassificationIds() {
        Set<Long> existingReclassificationIds = reclassificationSkupDao.findRecliassificationSkupIds();
        List<Long> remoteReclassificationIds = importer.getRemoteReclassificationIds();
        Set<Long> reclassificationIdsToBeCreated = new HashSet<Long>();
        boolean createNewReclassification = true;
        for (Long remoteId : remoteReclassificationIds) {
            createNewReclassification = true;
            for (Long localId : existingReclassificationIds) {
                if (Objects.equals(remoteId, localId)) {
                    createNewReclassification = false;
                    break;
                }
            }
            if (createNewReclassification) {
                reclassificationIdsToBeCreated.add(remoteId);
            }
        }
        return reclassificationIdsToBeCreated;
    }

    private static Long findPickerIdInBarcode(String barcode){
        while(barcode.length() < 13){
            barcode = "0" + barcode;
        }
        return Long.parseLong(barcode.substring(8,13));
    }

    private static Long findUniqIdInBarcode(String barcode){
        while(barcode.length() < 13){
            barcode = "0" + barcode;
        }
        return Long.parseLong(barcode.substring(3,8));
    }


}
