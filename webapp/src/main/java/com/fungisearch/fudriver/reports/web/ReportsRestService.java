package com.fungisearch.fudriver.reports.web;

import com.fungisearch.fudriver.reports.typesByPickers.dao.TypesByPickersDao;
import com.fungisearch.fudriver.reports.typesByPickers.dto.PickerDto;
import com.fungisearch.fudriver.reports.typesByPickers.dto.TypesByPickerDto;
import com.fungisearch.fudriver.reports.typesByPickers.dto.TypesByPickersDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/reports")
public class ReportsRestService {
    private final TypesByPickersDao typesByPickersDao;

    public ReportsRestService(TypesByPickersDao typesByPickersDao) {
        this.typesByPickersDao = typesByPickersDao;
    }

    @GetMapping("/typesByPickers/{startDate}/{endDate}")
    public List<TypesByPickersDto> getTypesByPickers(@PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                     @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return typesByPickersDao.getTypesByPicker(startDate,endDate);
    }

    @GetMapping("/typesByPicker/{pickerId}/{startDate}/{endDate}")
    public List<TypesByPickerDto> getTypesByPickers(@PathVariable(name = "pickerId") long pickerId,
                                                    @PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                    @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return typesByPickersDao.getPickersTypes(pickerId,startDate,endDate);
    }

    @GetMapping("/typesByPicker/picker/{pickerId}")
    public PickerDto getPicker(@PathVariable(name = "pickerId") int pickerId){
        return typesByPickersDao.getPicker(pickerId);
    }
}
