package com.fungisearch.fudriver.settings.query.dao;

import com.fungisearch.fudriver.settings.query.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface SettingsDao {
    int getValue(int id);
    Map<Long,String> getReclassifyReasons();
    List<CurrencyDto> getActiveCurencies();
    CompanyDto getCompany(long id);
    List<CompanyDto> getCompanies();
    Set<DepotDto> getActiveDepots();
    Set<ChamberDto> getActiveChambers();
    Set<SubsoilDto> getActiveSubsoils();
    Set<MyceliumDto> getActiveMyceliums();
}
