package com.fungisearch.fudriver.zarobki.query.dto;

import java.util.List;

/**
 * DTO for Zarobki->Statystyki report
 */
public class PickerZarobkiDto {
    public Long id;
    public Long nr;
    public String name;
    public String surname;
    public String groupName;
    public Long inne;
    public Long kraj;
    public Long export;
    public Long minutes;
    public Long checked;
    public Long rejected;
    public List<PickerZarobkiTypeGroupsDto> totalByGroups;

}