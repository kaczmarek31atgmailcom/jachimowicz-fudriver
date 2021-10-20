package com.fungisearch.fudriver.box.query.dao;

import com.fungisearch.fudriver.box.query.dto.BoxDto;

import java.util.List;

public interface BoxDao {
    public List<BoxDto> findActive();
}
