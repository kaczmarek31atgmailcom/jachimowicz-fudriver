package com.fungisearch.fudriver.reclassification.query.service;

import com.fungisearch.fudriver.reclassification.query.dao.ReclassificationSkupDao;
import com.fungisearch.fudriver.reclassification.query.dao.SkupRodzajDao;
import com.fungisearch.fudriver.reclassification.query.dto.LocalReclassificationDetailDto;
import com.fungisearch.fudriver.reclassification.query.dto.LocalRodzajDto;
import com.fungisearch.fudriver.reclassification.query.dto.ReclassificationPickerDto;
import com.fungisearch.fudriver.reclassification.query.dto.UnitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by marcin on 11.02.16.
 */
@Service
public class ReclassificationQueryService {

@Autowired
private SkupRodzajDao skupRodzajDao;

@Autowired
private ReclassificationSkupDao reclassificationSkupDao;

    public List<LocalReclassificationDetailDto> getReclassificationDetails(Long reclassificationId){
        List<LocalReclassificationDetailDto> details = reclassificationSkupDao.getDetails(reclassificationId);
        Map<Long,LocalRodzajDto> localTypes = skupRodzajDao.getMapOfAllLocalTypes();
        for (LocalReclassificationDetailDto detail: details){
            UnitDto unitDto = reclassificationSkupDao.findSingleUnit(detail.remotePickerId, detail.remoteUniqId);
            if(unitDto != null){
                detail.zarobkiId = unitDto.id;
                detail.pickerId = unitDto.pickerId;
                detail.pickerNr = unitDto.pickerNr;
                detail.pickerName = unitDto.pickerName;
                detail.pickerSurname = unitDto.pickerSurname;
                detail.halaName = unitDto.halaName;
                detail.pickerGroupName = unitDto.pickerGroupName;
                detail.wagowyLogin = unitDto.wagowyLogin;
                detail.wagowyName = unitDto.wagowyName;
                detail.wagowySurname = unitDto.wagowySurname;
                detail.harvestDate = unitDto.harvestDate;
                detail.payed = unitDto.payed;
                detail.originalTypeName = unitDto.typeName;
                detail.originalTypeWeight = unitDto.typeWeight;
                if(detail.afterReclassifcationTypeId > 0){
                    detail.reclassifcationTypeId = detail.afterReclassifcationTypeId;
                }
                if(localTypes.containsKey(detail.reclassifcationTypeId )){
                    detail.reclassificationTypeName = localTypes.get(detail.reclassifcationTypeId).name;
                    detail.reclassificationTypeWeight = localTypes.get(detail.reclassifcationTypeId).weight;
                }
            }

        }




        return details;
    }
}
