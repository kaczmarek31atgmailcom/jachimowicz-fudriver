package com.fungisearch.fudriver.reportForLeader.service;

import com.fungisearch.fudriver.chamber.query.dto.ChamberTypesDto;
import com.fungisearch.fudriver.reportForLeader.model.CollectedByChamber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class ReportForLeaderController {

    @Autowired
    private ReportForLeaderService reportForLeaderService;
/*
    @RequestMapping(value = "/rest/collectedByChamber", method = RequestMethod.GET)
    public @ResponseBody
    List<CollectedByChamber> getCollectedByChamber(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return reportForLeaderService.getCollectedByChumber(startDate, endDate);
    }
*/




}
