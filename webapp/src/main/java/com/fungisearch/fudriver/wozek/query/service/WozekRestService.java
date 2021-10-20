package com.fungisearch.fudriver.wozek.query.service;

import com.fungisearch.fudriver.cycle.query.dao.CycleDao;
import com.fungisearch.fudriver.cycle.query.dto.MassHarvestCycleDto;
import com.fungisearch.fudriver.cycle.query.dto.WozekCycleDto;
import com.fungisearch.fudriver.person.person.query.dao.PersonDao;
import com.fungisearch.fudriver.person.person.query.dto.PersonMassHarvestDto;
import com.fungisearch.fudriver.type.query.dao.TypeDao;
import com.fungisearch.fudriver.type.query.dto.MassHarvestTypeDto;
import com.fungisearch.fudriver.type.query.dto.WozekTypeDto;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.wozek.query.dao.WozekDao;
import com.fungisearch.fudriver.wozek.query.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by idea on 09.03.16.
 */
@RestController
public class WozekRestService {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private TypeDao typeDao;
    @Autowired
    private CycleDao cycleDao;
    @Autowired
    private WozekDao wozekDao;
    @Autowired
    private UserService userService;
    @Autowired
    WozekService wozekService;

    @RequestMapping(value = "/rest/wozek/people", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<ScannerPersonDto> getPeopleForScanner() {
        return personDao.findPeopleForScanner();
    }

    @RequestMapping(value = "/rest/wozek/type", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<WozekTypeDto> getTypes() {
        return typeDao.findTypesForWozek();
    }

    @RequestMapping(value = "/rest/wozek/cycle", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<WozekCycleDto> getCycles() {
        return cycleDao.findCyclesForWozek();
    }

    @RequestMapping(value = "/rest/wozek/{wozekId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<WozekEntryDto> getWozek(@PathVariable Long wozekId) {
        return wozekDao.getWozekStatus(wozekId);
    }


    @RequestMapping(value = "/rest/wozek/header", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<WozekHeaderDto> getWozekHeaders() {
        wozekService.generateTrolleyNumbers();
        return wozekDao.getWozekHeaders(userService.getCurrentUserId());
    }

    @RequestMapping(value = "/rest/wozek/trolleysSummary", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<TrolleysSummaryDto> getTrolleysSummary(){
        return wozekDao.getTrollyesSummary(userService.getCurrentUserId());
    }


    @RequestMapping(value = "/rest/mass-harvest/pickers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<PersonMassHarvestDto> getMassHarvestPickers() {
        return personDao.findMassHarverstPeople();
    }

    @RequestMapping(value = "/rest/mass-harvest/hale", method = RequestMethod.GET, produces = "application/json; charstet=UTF-8")
    List<MassHarvestCycleDto> getMassHarvestCycles(){
        return cycleDao.findCycleForMassHarvest();
    }

    @RequestMapping(value = "/rest/mass-harvest/types", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<MassHarvestTypeDto> getMassHarvestTypes(){
        return typeDao.findTypesForMassHarvest();
    }

    @RequestMapping(value = "/rest/wozek/total", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<TotalTrolleysStatusDto> getTotalTrolleysStatus(){
        return wozekDao.getTotalTrolleyStatus();
    }
}
