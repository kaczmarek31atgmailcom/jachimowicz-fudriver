package com.fungisearch.fudriver.zarobki.query.service.stand;

import com.fungisearch.fudriver.zarobki.query.dto.StandHeaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/rest/stand", produces = "application/json; charset=UTF-8")
public class StandRestService {

    private final StandService standService;

    @Autowired
    public StandRestService(StandService standService) {
        this.standService = standService;
    }

    @RequestMapping(value = "/pickerStats/{personId}/{yesterday}/{startPeriod}/{endPeriod}", method = RequestMethod.GET)
    public StandHeaderDto getPersonStats(@PathVariable(name = "personId") long personId,
                                         @PathVariable (value="yesterday") @DateTimeFormat(pattern="yyyy-MM-dd") Date yesterday,
                                         @PathVariable (value="startPeriod") @DateTimeFormat(pattern="yyyy-MM-dd") Date starPeriod,
                                         @PathVariable (value="endPeriod") @DateTimeFormat(pattern="yyyy-MM-dd") Date endPeriod){
        return standService.getStandStats(personId,yesterday,starPeriod,endPeriod);
    }
}
