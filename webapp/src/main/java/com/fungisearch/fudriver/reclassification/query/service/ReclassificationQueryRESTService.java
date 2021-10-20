package com.fungisearch.fudriver.reclassification.query.service;


import com.fungisearch.fudriver.reclassification.query.dao.ReclassificationSkupDao;
import com.fungisearch.fudriver.reclassification.query.dao.SkupRodzajDao;
import com.fungisearch.fudriver.reclassification.query.dto.LocalReclassificationDetailDto;
import com.fungisearch.fudriver.reclassification.query.dto.LocalReclassificationHeaderDto;
import com.fungisearch.fudriver.reclassification.query.dto.LocalRodzajDto;
import com.fungisearch.fudriver.reclassification.query.dto.SkupRodzajDto;
import com.fungisearch.fudriver.settings.model.Settings;
import com.fungisearch.fudriver.settings.query.dao.SettingsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by marcin on 02.02.16.
 */

@RestController
public class ReclassificationQueryRESTService {

    private final SkupRodzajDao skupRodzajDao;
    private final ReclassificationSkupDao reclassificationSkupDao;
    private final ReclassificationQueryService reclassificationQueryService;
    private final SettingsDao settingsDao;

    public ReclassificationQueryRESTService(SkupRodzajDao skupRodzajDao, ReclassificationSkupDao reclassificationSkupDao, ReclassificationQueryService reclassificationQueryService, SettingsDao settingsDao) {
        this.skupRodzajDao = skupRodzajDao;
        this.reclassificationSkupDao = reclassificationSkupDao;
        this.reclassificationQueryService = reclassificationQueryService;
        this.settingsDao = settingsDao;
    }

    @RequestMapping(value="/rest/reclassification/rodzaj", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    private List<SkupRodzajDto> getRemoteTypes(){
        return skupRodzajDao.getAllTypes();
    }

    @RequestMapping(value="/rest/reclassification/localRodzaj", params="active" , method = RequestMethod.GET, produces="application/json; charset=UTF-8")
    private List<LocalRodzajDto> getLocalTypes(@RequestParam(value = "active") Boolean active){
        if(active) {
            return skupRodzajDao.getActiveLocalTypes();
        } else {
            return skupRodzajDao.getAllLocalTypes();
        }
    }

    @RequestMapping(value="/rest/reclassification/localHeader", params = "processed", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    private List<LocalReclassificationHeaderDto> getLocalHeaders(@RequestParam(value="processed") Boolean processed){
        if(processed) {
            return reclassificationSkupDao.findProcessedLocalHeaders();
        } else {
            return reclassificationSkupDao.findNotProcessedLocalHeaders();
        }
    }

    @RequestMapping(value="/rest/reclassification/localHeader", params="headerId", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    private LocalReclassificationHeaderDto getLocalHeader(@RequestParam Long headerId){
        return reclassificationSkupDao.findLocalHeader(headerId);
    }


    @RequestMapping(value="/rest/reclassification/{id}/details", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    private List<LocalReclassificationDetailDto> getDetails(@PathVariable Long id){
        return reclassificationQueryService.getReclassificationDetails(id);
    }

    @RequestMapping(value="/rest/reclassificaton/reclassifyReason", method = RequestMethod.GET, produces = "application/json; charset=UTF8")
    public Map<Long,String> getReclassifyReasons(){
        return settingsDao.getReclassifyReasons();
    }

}
