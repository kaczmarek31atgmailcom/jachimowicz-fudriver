package com.fungisearch.fudriver.person.person.query.dao;

import com.fungisearch.fudriver.person.person.query.dto.*;
import com.fungisearch.fudriver.wozek.query.dto.ScannerPersonDto;

import java.util.List;


public interface PersonDao {

    List<PersonHeaderDto> getActiveHeaders();
    List<PersonHeaderDto> getAllHeaders();
    PersonDto getPerson(Long id);
    List<PersonGroupDto> getActiveGroups();
    List<PersonGroupDto> getGroups();
    List<Long> findFreeBarcodes(Long personId);
    List<ScannerPersonDto> findPeopleForScanner();
    List<PersonMassHarvestDto> findMassHarverstPeople();
    PersonHeaderDto findPersonByRfid(String rfid);
    WorkTimePersonHeaderDto findWorkTimePerson(Long id);
    List<Long> getReservedNumbers();
    ForeignerAlertDto getForeignerAlert();

    PersonDto getPersonRFID(String rfid);
    PersonDto getPersonRFID(Long rfid);
}
