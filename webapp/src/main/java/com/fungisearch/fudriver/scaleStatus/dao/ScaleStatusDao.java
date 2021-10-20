package com.fungisearch.fudriver.scaleStatus.dao;


import com.fungisearch.fudriver.scaleStatus.model.ScaleStatusDto;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ScaleStatusDao {

    List<ScaleStatusDto> getScalesStatus() throws UnsupportedEncodingException;
}
