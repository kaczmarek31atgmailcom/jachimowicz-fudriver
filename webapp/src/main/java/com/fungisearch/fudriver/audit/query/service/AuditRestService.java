package com.fungisearch.fudriver.audit.query.service;

import com.fungisearch.fudriver.audit.query.dto.LocalReclassificationAuditDto;
import com.fungisearch.fudriver.audit.query.dao.AuditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 03.08.16.
 */
@RestController
public class AuditRestService {

    @Autowired
    private AuditDao auditDao;

    @RequestMapping(value = "/rest/audit/localReclassification/{startDate}/{endDate}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<LocalReclassificationAuditDto> getLocalReclasifications(@PathVariable(value="startDate") @DateTimeFormat(pattern="yyyyMMddHHmmss") Date startDate,
                                                                 @PathVariable(value="endDate") @DateTimeFormat(pattern="yyyyMMddHHmmss") Date endDate){
        return auditDao.findReclassifications(startDate,endDate);
    }
}
