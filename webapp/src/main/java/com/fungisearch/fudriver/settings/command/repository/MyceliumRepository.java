package com.fungisearch.fudriver.settings.command.repository;


import com.fungisearch.fudriver.settings.command.model.Mycelium;

public interface MyceliumRepository {
    void save(Mycelium mycelium);
    Mycelium find(long id);
}
