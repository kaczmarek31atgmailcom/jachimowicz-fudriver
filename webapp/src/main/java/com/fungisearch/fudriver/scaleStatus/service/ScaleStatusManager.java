package com.fungisearch.fudriver.scaleStatus.service;

import com.fungisearch.fudriver.scaleStatus.dao.ScaleStatusDao;
import com.fungisearch.fudriver.scaleStatus.model.ScaleStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScaleStatusManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScaleStatusDao scaleStatusDao;

    public List<ScaleStatusDto> getScalesStatus(){
        List<ScaleStatusDto> result = new ArrayList<ScaleStatusDto>();
        try {
             result = scaleStatusDao.getScalesStatus();
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding exception " + e.getMessage());
        }
    return result;
    }
}
