package com.fungisearch.fudriver.type.command.repository;

import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeGroup;
import com.fungisearch.fudriver.type.command.model.TypeSize;

import java.util.List;


public interface TypeRepository {
    List<Type> findAll();
    Type findById(Long id);
    void save(Type type);

    void save(TypeGroup typeGroup);

    TypeGroup findTypeGroup(long id);

    void save(TypeSize typeSize);

    TypeSize findTypeSize(Long id);
}
