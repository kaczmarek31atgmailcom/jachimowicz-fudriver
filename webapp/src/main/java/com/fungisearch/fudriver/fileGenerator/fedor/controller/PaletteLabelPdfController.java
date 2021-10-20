package com.fungisearch.fudriver.fileGenerator.fedor.controller;

import com.fungisearch.fudriver.fileGenerator.fedor.model.PaletteLabel;
import com.fungisearch.fudriver.paletteType.query.dao.PaletteDao;
import com.fungisearch.fudriver.settings.query.dao.SettingsDao;
import com.fungisearch.fudriver.settings.query.dto.CompanyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by marcin on 27.07.16.
 */
@Controller
public class PaletteLabelPdfController {

    @Autowired
    private PaletteDao paletteDao;
    @Autowired
    private SettingsDao settingsDao;

    @RequestMapping(value = "/generated/pdf/palette-label.pdf", method = RequestMethod.GET)
    public ModelAndView generatePaletteLabel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long paletteId = Long.parseLong(request.getParameter("paletteId"));
        PaletteLabel paletteLabel = paletteDao.generatePaletteLabel(paletteId);
        CompanyDto companyDto = settingsDao.getCompany(1L);
        paletteLabel.setCompanyName(companyDto.name);
        paletteLabel.setCompanyCity(companyDto.city);
        paletteLabel.setCompanyStreet(companyDto.street);
        paletteLabel.setCompanyNip(companyDto.nip);
        paletteLabel.setCompanyRegon(companyDto.regon);
        paletteLabel.setCompanyEmail(companyDto.email);
        paletteLabel.setCompanyPhoneNo(companyDto.phoneNo);
        return new ModelAndView("paletteLabelView", "command", paletteLabel);
    }
}
