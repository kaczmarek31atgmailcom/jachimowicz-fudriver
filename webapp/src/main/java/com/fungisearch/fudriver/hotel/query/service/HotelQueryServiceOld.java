package com.fungisearch.fudriver.hotel.query.service;

import com.fungisearch.fudriver.hotel.query.dao.HotelDao;
import org.springframework.stereotype.Service;

@Service
public class HotelQueryServiceOld {
    private final HotelDao hotelDao;

    public HotelQueryServiceOld(HotelDao hotelDao) {
        this.hotelDao = hotelDao;
    }
/*
    public List<BedStatusDto> getBedStatus(Date startDate, Date endDate, long hotelId) {
        List<HotelBedDto> beds = hotelDao.getBeds(hotelId);
        List<BedOccupancyDto> occupancy = hotelDao.getBedsStatus(startDate, endDate, hotelId);
        List<Date> days = DateUtils.getDaysBetweenDates(startDate, endDate);
        List<BedStatusDto> result = new ArrayList<>();
        for (Date day : days) {
            result.addAll(beds.stream().map(b -> getBedStatus(occupancy, b, day)).collect(Collectors.toList()));
        }
        return result;
    }

    public HotelBedPeridDto getBedPeriod(long bedId, Date day) {
        HotelBedPeridDto dto = new HotelBedPeridDto();
        BedOccupancyDto occupancyDto = hotelDao.getBedStatus(bedId, day);
        if(occupancyDto.hotelBedStatus == null) {
            dto.startDate = hotelDao.getMaxDateBeforeDate(bedId, day);
            dto.endDate = hotelDao.getMinDateAfterDate(bedId, day);
            dto.hotelBedStatus = HotelBedStatus.UNOCCUPIED;
        } else if (occupancyDto.hotelBedStatus.equals(HotelBedStatus.OCCUPIED)) {
            List<BedOccupancyDto> bedOccupancyDtos = hotelDao.getBedStatus(bedId);
            Collections.sort(bedOccupancyDtos);
            dto.hotelBedStatus = occupancyDto.hotelBedStatus;
            dto.startDate = findBeginningOfTheOccupiedPeriod(occupancyDto,bedOccupancyDtos).date;
            dto.endDate = findEndOfTheOccupiedPeriod(occupancyDto,bedOccupancyDtos).date;
        } else if (occupancyDto.hotelBedStatus.equals(HotelBedStatus.RESERVED)) {
            List<BedOccupancyDto> bedOccupancyDtos = hotelDao.getBedStatus(bedId);
            Collections.sort(bedOccupancyDtos);
            dto.hotelBedStatus = occupancyDto.hotelBedStatus;
            dto.startDate = findBeginningOfTheReservedPeriod(occupancyDto,bedOccupancyDtos).date;
            dto.endDate = findEndOfTheReservedPeriod(occupancyDto,bedOccupancyDtos).date;
        }
        return dto;
    }

    public BedOccupancyDto findBeginningOfTheReservedPeriod(BedOccupancyDto dto, List<BedOccupancyDto> list) {
        BedOccupancyDto tested = findBedOccupancyDto(dto.bedId, DateUtils.getYesterday(dto.date), list);
        if (tested == null ||!tested.hotelBedStatus.equals(HotelBedStatus.RESERVED)) {
            return dto;
        } else {
            return findBeginningOfTheReservedPeriod(tested, list);
        }
    }

    public BedOccupancyDto findEndOfTheReservedPeriod(BedOccupancyDto dto, List<BedOccupancyDto> list) {
        BedOccupancyDto tested = findBedOccupancyDto(dto.bedId, DateUtils.getTomorrow(dto.date), list);
        if (tested == null  || !tested.hotelBedStatus.equals(HotelBedStatus.RESERVED)) {
            return dto;
        } else {
            return findEndOfTheReservedPeriod(tested, list);
        }
    }

    public BedOccupancyDto findBeginningOfTheOccupiedPeriod(BedOccupancyDto dto, List<BedOccupancyDto> list) {
        BedOccupancyDto tested = findBedOccupancyDto(dto.bedId, DateUtils.getYesterday(dto.date), list);
        if (tested == null || !tested.personId.equals(dto.personId) || !tested.hotelBedStatus.equals(HotelBedStatus.OCCUPIED)) {
            return dto;
        } else {
            return findBeginningOfTheOccupiedPeriod(tested, list);
        }
    }

    public BedOccupancyDto findEndOfTheOccupiedPeriod(BedOccupancyDto dto, List<BedOccupancyDto> list) {
        BedOccupancyDto tested = findBedOccupancyDto(dto.bedId, DateUtils.getTomorrow(dto.date), list);
        if (tested == null || !tested.personId.equals(dto.personId) || !tested.hotelBedStatus.equals(HotelBedStatus.OCCUPIED)) {
            return dto;
        } else {
            return findEndOfTheOccupiedPeriod(tested, list);
        }
    }


    private BedOccupancyDto findBedOccupancyDto(long bedId, Date day, List<BedOccupancyDto> theList) {
        return theList.stream().filter(o -> o.bedId == bedId).filter(o -> DateUtils.getStartOfDay(o.date).equals(DateUtils.getStartOfDay(day))).findFirst().orElse(null);
    }

    private BedStatusDto getBedStatus(List<BedOccupancyDto> occupancy, HotelBedDto bed, Date day) {
        BedStatusDto bedStatusDto = new BedStatusDto();
        bedStatusDto.bedId = bed.bedId;
        bedStatusDto.bedName = bed.bedName;
        bedStatusDto.date = day;
        bedStatusDto.roomId = bed.roomId;
        bedStatusDto.roomName = bed.roomName;

        BedOccupancyDto bedOccupancyDto = occupancy.stream().filter(o -> o.bedId == bed.bedId).filter(o -> o.date.equals(day)).findFirst().orElse(null);
        if (bedOccupancyDto == null) {
            bedStatusDto.hotelBedStatus = HotelBedStatus.UNOCCUPIED;
        } else {
            bedStatusDto.hotelBedStatus = bedOccupancyDto.hotelBedStatus;
            bedStatusDto.description = bedOccupancyDto.description;
            bedStatusDto.personId = bedOccupancyDto.personId;
            bedStatusDto.personName = bedOccupancyDto.personName;
            bedStatusDto.personSurname = bedOccupancyDto.personSurname;
        }
        return bedStatusDto;
    }
*/
}
