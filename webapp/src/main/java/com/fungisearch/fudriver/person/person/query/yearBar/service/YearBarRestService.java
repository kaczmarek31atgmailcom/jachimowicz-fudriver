package com.fungisearch.fudriver.person.person.query.yearBar.service;

import com.fungisearch.fudriver.person.person.query.yearBar.dto.SimpleWorkPeriodDto;
import com.fungisearch.fudriver.person.person.query.yearBar.model.YearBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by marcin on 10.01.16.
 */
@RestController
public class YearBarRestService {

    @Autowired
    private YearBarService yearBarService;

    @RequestMapping(value = "/rest/person/yearBars/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public List<YearBar> findOne(@PathVariable Long id){
        return yearBarService.getYearBars(id);
    }

    @RequestMapping(value = "/rest/person/workPeriod/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<SimpleWorkPeriodDto> findSimpleWorkPeriods(@PathVariable Long id){
    return yearBarService.getSimpleWorkPeriods(id);
    }
}
