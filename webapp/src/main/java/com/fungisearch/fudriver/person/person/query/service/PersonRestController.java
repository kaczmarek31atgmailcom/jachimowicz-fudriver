package com.fungisearch.fudriver.person.person.query.service;

import com.fungisearch.fudriver.person.person.query.dao.PersonDao;
import com.fungisearch.fudriver.person.person.query.dto.ForeignerAlertDto;
import com.fungisearch.fudriver.person.person.query.dto.PersonDto;
import com.fungisearch.fudriver.person.person.query.dto.PersonGroupDto;
import com.fungisearch.fudriver.person.person.query.dto.PersonHeaderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by idea on 28.02.16.
 */
@RestController
public class PersonRestController {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PersonHeadersService personHeadersService;

    @RequestMapping(value = "/rest/person/headers", params="active", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<PersonHeaderDto> getHeaders(@RequestParam(value="active") Boolean active){
        List<PersonHeaderDto> result;
        if(active){
            result = personHeadersService.getActiveHeaders();
        }else {
            result = personHeadersService.getAllHeaders();
        }
        return result;
    }

    @RequestMapping(value = "/rest/person/{employeeId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    PersonDto getPerson(@PathVariable Long employeeId){
        return personDao.getPerson(employeeId);
    }


    @RequestMapping(value = "/rest/person/rfid/{rfid}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    PersonHeaderDto getPersonByRfid(@PathVariable String rfid){
        return personDao.findPersonByRfid(rfid);
    }


    @RequestMapping(value = "/rest/person/group", params="active", method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<PersonGroupDto> getGroups(@RequestParam(value=  "active") Boolean active){
        if (active){
            return personDao.getActiveGroups();
        } else {
            return personDao.getGroups();
        }
    }

    @RequestMapping(value = "/rest/person/barcode/notUsed/{employeeId}",  method=RequestMethod.GET, produces = "application/json; charset=UTF-8" )
    public List<Long> getFreeBarcodes(@PathVariable Long employeeId){
        List<Long> freeNumbers = personDao.findFreeBarcodes(employeeId);
        Collections.sort(freeNumbers);
        return freeNumbers;
    }

    @RequestMapping(value = "/rest/person/reservedNumbers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<Long> getReservedNumbers(){
        return personDao.getReservedNumbers();
    }

    @RequestMapping(value = "/rest/person/foreignerAlert", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ForeignerAlertDto getForeingerAlert(){
        return personDao.getForeignerAlert();
    }
}
