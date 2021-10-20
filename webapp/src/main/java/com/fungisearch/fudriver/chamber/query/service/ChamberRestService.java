package com.fungisearch.fudriver.chamber.query.service;

import com.fungisearch.fudriver.chamber.query.dto.ChamberTypesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 01.03.17.
 */
@RestController
public class ChamberRestService {

    private final ChamberReportService chamberReportService;

    @Autowired
    public ChamberRestService(ChamberReportService chamberReportService) {
        this.chamberReportService = chamberReportService;
    }

    @RequestMapping(value = "/rest/collectedByChamber/{startDate}/{endDate}", method = RequestMethod.GET)
    public List<ChamberTypesDto> getTypesByChambers(@PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,
                                                    @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
        return chamberReportService.getChamberTypes(startDate, endDate);
    }
}
