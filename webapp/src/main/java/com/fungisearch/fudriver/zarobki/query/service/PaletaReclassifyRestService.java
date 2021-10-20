package com.fungisearch.fudriver.zarobki.query.service;

import com.fungisearch.fudriver.cycle.query.dao.CycleDao;
import com.fungisearch.fudriver.cycle.query.dto.WozekCycleDto;
import com.fungisearch.fudriver.type.query.dao.TypeDao;
import com.fungisearch.fudriver.type.query.dto.WozekTypeDto;
import com.fungisearch.fudriver.zarobki.query.dao.PaletaDao;
import com.fungisearch.fudriver.zarobki.query.dto.PaletaDetailsDto;
import com.fungisearch.fudriver.zarobki.query.dto.PaletaDetailsHeaderDto;
import com.fungisearch.fudriver.zarobki.query.dto.PaletaHeaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 12.04.16.
 */
@RestController
public class PaletaReclassifyRestService {

    @Autowired
    private PaletaDao paletaDao;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private CycleDao cycleDao;

    @RequestMapping(value = "/rest/reclassify/paletaHeader", method=RequestMethod.GET ,produces = "application/json; charset=UTF-8")
    public List<PaletaHeaderDto> getPalety(@RequestParam(value="dateFrom") @DateTimeFormat(pattern="yyyyMMdd")Date dateFrom, @RequestParam(value="dateTo") @DateTimeFormat(pattern="yyyyMMdd") Date dateTo){
        return paletaDao.getPaletaHeaders(dateFrom,dateTo);
    }

    @RequestMapping(value = "/rest/reclassify/paletaDetails/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<PaletaDetailsDto> getPaleta(@PathVariable Long id){
        return paletaDao.getPaletaDetails(id);
    }

    @RequestMapping(value = "/rest/reclassify/types", method=RequestMethod.GET, produces="application/json; charset=UTF-8")
    public List<WozekTypeDto> getActiveTypes(){
        return typeDao.findTypesForWozek();
    }

    @RequestMapping(value = "/rest/reclassify/cycles", method = RequestMethod.GET, produces="application/json; charset=UTF-8")
    public List<WozekCycleDto> getCycles(){
        return cycleDao.findCyclesForWozek();
    }

    @RequestMapping(value = "/rest/reclassify/paletaHeader/{nr}", method=RequestMethod.GET, produces="application/json; charset=UTF-8")
    public PaletaDetailsHeaderDto getHeader(@PathVariable Long nr){
        return paletaDao.getPaletaHeader(nr);
    }
}
