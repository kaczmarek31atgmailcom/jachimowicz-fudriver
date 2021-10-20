package com.fungisearch.fudriver.wozek.command.repository;

import com.fungisearch.fudriver.wozek.command.model.Uniq;

import java.util.List;

/**
 * Created by marcin on 23.02.16.
 */
public interface UniqRepository {

    //void update(Uniq uniq);
    void save(Uniq uniq);
    void delete(Uniq uniq);
    List<Uniq> findOpenForPicker(Long pickerId);
    Uniq findTransactional(Long pickerId, Long uniqId);
    Long findHighestUniqIdForPicker(Long pickerId);
}
