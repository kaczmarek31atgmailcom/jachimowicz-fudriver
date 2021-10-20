package com.fungisearch.fudriver.zarobki.query.service;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.zarobki.query.dto.PickerZarobkiDto;
import com.fungisearch.fudriver.zarobki.query.dto.ScannerManReportTotalsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 12.09.16.
 */
@RestController
public class ZarobkiRestService {


    private final PickerZarobkiService outcomeService;
    private final ScannerManReportService scannerManReportService;

    @Autowired
    public ZarobkiRestService(PickerZarobkiService outcomeService, ScannerManReportService scannerManReportService) {
        this.outcomeService = outcomeService;
        this.scannerManReportService = scannerManReportService;
    }

    @RequestMapping(value = "/rest/zarobki/reportForLeader/{startDate}/{endDate}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<PickerZarobkiDto> getReportForLeader(@PathVariable (value="startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                              @PathVariable (value="endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) throws ParseException {
        return outcomeService.getReportForLeader(DateUtils.getStartOfDay(startDate),DateUtils.getEndOfDay(endDate));
    }

    @RequestMapping(value = "/rest/zarobki/scannerManReport/{startDate}/{endDate}",method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<ScannerManReportTotalsDto> getScannerManActivity(@PathVariable(value="startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                                          @PathVariable(value="endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return scannerManReportService.getScannerManActivity(DateUtils.getStartOfDay(startDate),DateUtils.getEndOfDay(endDate));
    }
}
