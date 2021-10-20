package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.Command;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;

import java.util.Date;

/**
 * Created by idea on 04.03.16.
 */
public class AddPersonCommand implements Command {

    public Long nr;
    public String imie;
    public String nazwisko;
    public String adres;
    public String mobile;
    public Long groupId;
    public Date birthDate;
    public String city;
    public String rejon;
    public String indeks;
    public String imionaRodzicow;
    public String nrWizy;
    public Date koniecWaznosciWizy;
    public String pesel;
    public Date poczatekWaznosciZezwolenia;
    public Date koniecWaznosciZezwolenia;
    public Date dataZameldowania;
    public Date dataWymeldowania;
    public Date koniecWaznosciPaszportu;
    public String nrPaszportu;
    public String nrOswiadczenia;
    public String nrZezwolenia;
    public Boolean isForeigner;
}
