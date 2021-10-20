package com.fungisearch.fudriver.box.command.repository;

import com.fungisearch.fudriver.box.command.model.Box;


public interface BoxRepository {
    void create(Box box);
    Box find (long id);
}
