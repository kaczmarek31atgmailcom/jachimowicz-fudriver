package com.fungisearch.fudriver.chamber.query.service;

import com.fungisearch.fudriver.chamber.query.dao.ChamberDao;
import com.fungisearch.fudriver.chamber.query.dto.ChamberTypeDto;
import com.fungisearch.fudriver.chamber.query.dto.ChamberTypesDto;
import com.fungisearch.fudriver.common.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 01.03.17.
 */
@Service
public class ChamberReportService {

    private final ChamberDao chamberDao;

    @Autowired
    public ChamberReportService(ChamberDao chamberDao) {
        this.chamberDao = chamberDao;
    }

    public List<ChamberTypesDto> getChamberTypes(Date startDate, Date endDate) {
        List<ChamberTypesDto> list = chamberDao.getHarvestByChamber(DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate));
        list = setTotals(list);
        list = setExportRatio(list);
        return list;
    }

    private List<ChamberTypesDto> setTotals(List<ChamberTypesDto> list) {
        for (ChamberTypesDto chamber : list) {
            for (ChamberTypeDto type : chamber.types) {
                if (type.exportType == 1) {
                    chamber.krajTotal += type.totalWeight;
                }
                if (type.exportType == 2) {
                    chamber.exportTotal += type.totalWeight;
                }
                if (type.exportType == 3) {
                    chamber.inneTotal += type.totalWeight;
                }
            }
        }
        return list;
    }

    private List<ChamberTypesDto> setExportRatio(List<ChamberTypesDto> list) {
        for (ChamberTypesDto chamber : list) {
            if ((chamber.krajTotal + chamber.exportTotal) > 0) {
                chamber.exportRatio = (chamber.exportTotal / (chamber.exportTotal + chamber.krajTotal)) * 100;
            }
        }
        return list;
    }
}
