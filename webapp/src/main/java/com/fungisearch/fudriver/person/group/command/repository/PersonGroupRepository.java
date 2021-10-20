package com.fungisearch.fudriver.person.group.command.repository;

import com.fungisearch.fudriver.person.group.command.model.PersonGroup;

/**
 * Created by marcin on 04.01.17.
 */
public interface PersonGroupRepository {
    void save(PersonGroup personGroup);
}
