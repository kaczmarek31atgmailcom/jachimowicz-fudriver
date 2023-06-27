package com.fungisearch.fudriver.wozek.query.service;

import com.fungisearch.fudriver.wozek.query.dao.UniqDao;
import com.fungisearch.fudriver.wozek.query.dto.CheckCodeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/uniq/")
public class UniqueRestService {

    private final UniqDao uniqDao;

    public UniqueRestService(UniqDao uniqDao) {
        this.uniqDao = uniqDao;
    }

    @GetMapping(value = "/checkCode/pickerId/{pickerId}/uniqId/{uniqId}")
    public CheckCodeDto checkCode(@PathVariable(name = "uniqId") long uniqId,
                                  @PathVariable(name = "pickerId") long pickerId){
        return uniqDao.checkBarcode(uniqId,pickerId);
    }
}
