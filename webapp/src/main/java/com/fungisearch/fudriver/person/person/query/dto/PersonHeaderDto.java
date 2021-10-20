package com.fungisearch.fudriver.person.person.query.dto;

import java.util.Date;

public class PersonHeaderDto {
    public Long id;
    public Long nr;
    public String name;
    public String surname;
    public String groupName;
    public Boolean active;
    public Boolean isPresent;
    public boolean isForeigner;
    public Date koniecWaznosciPaszportu;
    public Date koniecWaznosciZezwolenia;
    public Date koniecWaznosciWizy;
    public Date dataWymeldowania;
    public boolean isAlertFired = false;
}
