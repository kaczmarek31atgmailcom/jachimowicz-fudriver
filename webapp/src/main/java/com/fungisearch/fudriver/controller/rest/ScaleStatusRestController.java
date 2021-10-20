package com.fungisearch.fudriver.controller.rest;

import com.fungisearch.fudriver.scaleStatus.model.ScaleStatusDto;
import com.fungisearch.fudriver.scaleStatus.service.ScaleStatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScaleStatusRestController {

    @Autowired
    private ScaleStatusManager scaleStatusManager;

    @RequestMapping(value = "/rest/scalesStatus", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ScaleStatusDto> showStatus(){
        return scaleStatusManager.getScalesStatus();
    }

}
