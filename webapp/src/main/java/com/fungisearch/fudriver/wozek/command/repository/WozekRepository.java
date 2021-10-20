package com.fungisearch.fudriver.wozek.command.repository;

import com.fungisearch.fudriver.wozek.command.model.WozekEntry;

import java.util.List;

/**
 * Created by marcin on 23.02.16.
 */
public interface WozekRepository {

    void save(WozekEntry wozekEntry);
    WozekEntry findOne(Long id);
    void delete(WozekEntry wozekEntry);
    List<WozekEntry> getEntriesForWozekId(Long wozekId);
    Long getTotalAmount(Long nr);

    WozekEntry findByPickerAndUniq(Long pickerId, Long uniqId);
}
