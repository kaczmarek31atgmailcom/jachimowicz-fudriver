package com.fungisearch.fudriver.zarobki.query.service.stand;

import com.fungisearch.fudriver.timeRecorder.query.dao.WorkTimeLogDao;
import com.fungisearch.fudriver.timeRecorder.query.dto.WorkMinutesDto;
import com.fungisearch.fudriver.zarobki.query.dto.StandHeaderDto;
import com.fungisearch.fudriver.zarobki.query.dto.StandRFIDHeaderDto;
import com.fungisearch.fudriver.zarobki.query.dto.StandWageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/stand", produces = "application/json; charset=UTF-8")
public class StandRestService {

    private final StandService standService;
    private final WorkTimeLogDao workTimeLogDao;

    @Autowired
    public StandRestService(StandService standService, WorkTimeLogDao workTimeLogDao) {
        this.standService = standService;
        this.workTimeLogDao = workTimeLogDao;
    }

    @RequestMapping(value = "/pickerStats/{personId}/{yesterday}/{startPeriod}/{endPeriod}", method = RequestMethod.GET)
    public StandHeaderDto getPersonStats(@PathVariable(name = "personId") long personId,
                                         @PathVariable (value="yesterday") @DateTimeFormat(pattern="yyyy-MM-dd") Date yesterday,
                                         @PathVariable (value="startPeriod") @DateTimeFormat(pattern="yyyy-MM-dd") Date starPeriod,
                                         @PathVariable (value="endPeriod") @DateTimeFormat(pattern="yyyy-MM-dd") Date endPeriod){
        return standService.getStandStats(personId,yesterday,starPeriod,endPeriod);
    }

    @RequestMapping(value = "/pickerStatsRFID/{rfid}", method = RequestMethod.GET)
    public StandRFIDHeaderDto getPersonStatsRfid(@PathVariable(name = "rfid") String rfid) {
        return standService.getStandStatsRfid(rfid);
    }

    @GetMapping("/pickerStatsRFID/{rfid}/workMinutes")
    public List<WorkMinutesDto> getWorkMinutes(@PathVariable(value = "rfid") String rfid) {
        return workTimeLogDao.getWorkMinutes(rfid);
    }

}
