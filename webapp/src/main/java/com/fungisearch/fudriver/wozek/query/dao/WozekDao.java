package com.fungisearch.fudriver.wozek.query.dao;

import com.fungisearch.fudriver.wozek.query.dto.TotalTrolleysStatusDto;
import com.fungisearch.fudriver.wozek.query.dto.TrolleysSummaryDto;
import com.fungisearch.fudriver.wozek.query.dto.WozekEntryDto;
import com.fungisearch.fudriver.wozek.query.dto.WozekHeaderDto;

import java.util.List;
import java.util.Map;

/**
 * Created by idea on 13.03.16.
 */
public interface WozekDao {
    List<WozekEntryDto> getWozekStatus(Long wozekId);
    Map<Long, Long> getNumbersOnTrolley(Long userId);
    void setTrolleyNumber(Long userId, Long rodzajId,Long trolleyNumber);
    List<WozekHeaderDto> getWozekHeaders(Long userId);
    void sendForApproval(Long wozekId);
    void onHold(Long wozekId);
    void activate(Long wozekId);
    Boolean isTrolleyReadyToActivate(Long wozekId, Long userId);
    List<TotalTrolleysStatusDto> getTotalTrolleyStatus();
    List<TrolleysSummaryDto> getTrollyesSummary(long userId);
}
