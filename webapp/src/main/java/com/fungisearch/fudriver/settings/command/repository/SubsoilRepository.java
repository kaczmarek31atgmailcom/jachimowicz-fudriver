package com.fungisearch.fudriver.settings.command.repository;


import com.fungisearch.fudriver.settings.command.model.Subsoil;

public interface SubsoilRepository {
    void save(Subsoil subsoil);
    Subsoil find(long id);
}
