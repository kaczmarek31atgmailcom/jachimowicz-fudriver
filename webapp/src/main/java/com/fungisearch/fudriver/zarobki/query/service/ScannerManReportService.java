package com.fungisearch.fudriver.zarobki.query.service;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.zarobki.query.dao.ScannerManReportDao;
import com.fungisearch.fudriver.zarobki.query.dto.ScannerManReportTotalsDto;
import com.fungisearch.fudriver.zarobki.query.dto.ScannerManReportTypeDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScannerManReportService {

    private final ScannerManReportDao scannerManReportDao;

    @Autowired
    public ScannerManReportService(ScannerManReportDao scannerManReportDao) {
        this.scannerManReportDao = scannerManReportDao;
    }

    public List<ScannerManReportTotalsDto> getScannerManActivity(Date startDate, Date endDate) {
        List<ScannerManReportTotalsDto> headers = scannerManReportDao.getTotals(DateUtils.getStartOfDay(startDate),DateUtils.getEndOfDay(endDate));
        List<ScannerManReportTypeDetailDto> details = scannerManReportDao.getDetails(DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate));
        List<TypeDto> typeDtos = getAllTypeDtos(details);
        for(ScannerManReportTotalsDto header: headers){
            header.details = getDetails(header.personId,typeDtos,details);
        }
        return headers;
    }

    private List<ScannerManReportTypeDetailDto> getDetails(Long personId, List<TypeDto> typeDtos, List<ScannerManReportTypeDetailDto> details) {
        List<ScannerManReportTypeDetailDto> result = new ArrayList<>();
        for(TypeDto typeDto: typeDtos){
            result.add(findDetail(typeDto,personId,details));
        }
        return result;
    }

    private ScannerManReportTypeDetailDto findDetail(TypeDto typeDto, Long personId, List<ScannerManReportTypeDetailDto> details) {
        return details.stream()
                .filter(p->new Long(p.typeId).equals(typeDto.id))
                .filter(p->new Long(p.personId).equals(personId))
                .findFirst()
                .orElse(CreateZeroDetail(typeDto, personId));
    }

    private ScannerManReportTypeDetailDto CreateZeroDetail(TypeDto typeDto, Long personId) {
        ScannerManReportTypeDetailDto detail;
        detail = new ScannerManReportTypeDetailDto();
        detail.personId = personId.intValue();
        detail.typeId = new Long(typeDto.id).intValue();
        detail.typeName = typeDto.name;
        detail.typeWeight = new Long(typeDto.weigth).intValue();
        detail.exportType = typeDto.exportType;
        detail.totalWeight = 0L;
        detail.numberPcs = 0L;
        return detail;
    }


    private List<TypeDto> getAllTypeDtos(List<ScannerManReportTypeDetailDto> details) {
        Set<Integer> hash = details.stream()
                .map(ScannerManReportTypeDetailDto::getTypeId)
                .collect(Collectors.toSet());
        List<TypeDto> dtos = new ArrayList<>();
        for (Integer id : hash) {
            dtos.add(findTypeDtoById(id, details));
        }
        dtos.sort(new TypeDtoComparator());
        return dtos;
    }

    private TypeDto findTypeDtoById(Integer typeId, List<ScannerManReportTypeDetailDto> details) {
        ScannerManReportTypeDetailDto dto = details.stream()
                .filter(d -> d.typeId.equals(typeId))
                .findFirst()
                .get();
        TypeDto typeDto = new TypeDto();
        typeDto.id = new Long(dto.typeId);
        typeDto.name = dto.typeName;
        typeDto.weigth = dto.typeWeight;
        typeDto.exportType = dto.exportType;
        return typeDto;
    }

    private class TypeDto {
        public Long id;
        public String name;
        public long weigth;
        public ExportType exportType;
    }

    private class TypeDtoComparator implements Comparator<TypeDto> {
        @Override
        public int compare(TypeDto o1, TypeDto o2) {
            if (o1.exportType.index == o2.exportType.index) {
                return new Long(o1.id).compareTo(new Long(o2.id));
            } else {
                return new Integer(o1.exportType.index).compareTo(new Integer(o2.exportType.index));
            }
        }
    }

}
