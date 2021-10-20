package com.fungisearch.fudriver.warehouseEastMushrooms.query.dao;

import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteStatus;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnitStatus;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dto.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class EastWarehouseDao {
    private final JdbcTemplate jdbcTemplate;

    public EastWarehouseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<HarvestPaletteDto> getWaitingHarvestPalettes() {
        return jdbcTemplate.query("select " +
                "       w.wozek_nr as paletteId, " +
                "       u.user_name as creatorLogin, " +
                "       r.name as typeName, " +
                "       r.waga as typeWeight, " +
                "       count(*) as amount " +
                " from wozek w, " +
                "     users u, " +
                "     rodzaj r " +
                " where u.id = w.user_id " +
                "  and r.id = w.rodzaj_id " +
                "  and w.przestrzen_id = 1 " +
                " group by w.wozek_nr", new HarvestPaletteResultSetExtractor());
    }


    private class HarvestPaletteResultSetExtractor implements ResultSetExtractor<List<HarvestPaletteDto>> {
        @Override
        public List<HarvestPaletteDto> extractData(ResultSet rs) throws SQLException {
            List<HarvestPaletteDto> list = new ArrayList<>();
            while (rs.next()) {
                HarvestPaletteDto dto = new HarvestPaletteDto();
                dto.paletteId = rs.getLong("paletteId");
                dto.creatorLogin = rs.getString("creatorLogin");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getDouble("typeWeight");
                dto.amount = rs.getInt("amount");
                list.add(dto);
            }
            return list;
        }
    }

    public List<CreatedWarehousePaletteDto> getCreatedWarehousePalettes() {
        return jdbcTemplate.query("select p.id as warehousePaletteId, " +
                        "        p.palette_type_id as paletteTypeId, " +
                        "        u.id as unitId, " +
                        "        u.uniq_id as uniqId,     " +
                        "        u.harvest_palette_id as harvestPaletteId,     " +
                        "        u.picker_id as pickerId,     " +
                        "        l.imie as pickerName,     " +
                        "        l.nazwisko as pickerSurname,     " +
                        "        u.cycle_id as cycleId,     " +
                        "        h.name as chamberName,     " +
                        "        r.name as typeName,     " +
                        "        r.waga as typeWeight,     " +
                        "        u.harvest_time as harvestTime,     " +
                        "        u.status as warehouseStatus " +
                        " from east_mushrooms_warehouse_palette p " +
                        " left join east_mushrooms_warehouse_unit u on u.palette_id = p.id and u.status = 0 " +
                        " left join ludzie l on l.id = u.picker_id     " +
                        " left join cykle c on c.id = u.cycle_id     " +
                        " left join hala h on h.id = c.hala_id     " +
                        " left join rodzaj r on r.id = u.local_type_id " +
                        " where p.status = 0",
                new CreatedWarehousePalettesResultSetExtractor());
    }

    private class CreatedWarehousePalettesResultSetExtractor implements ResultSetExtractor<List<CreatedWarehousePaletteDto>> {
        @Override
        public List<CreatedWarehousePaletteDto> extractData(ResultSet rs) throws SQLException {
            Map<Long, CreatedWarehousePaletteDto> map = new HashMap<>();
            while (rs.next()) {
                long paletteId = rs.getLong("warehousePaletteId");
                CreatedWarehousePaletteDto dto;
                if (map.containsKey(paletteId)) {
                    dto = map.get(paletteId);
                } else {
                    dto = new CreatedWarehousePaletteDto();
                    dto.paletteId = paletteId;
                    dto.paletteTypeId = rs.getInt("paletteTypeId");
                    dto.warehouseUnits = new ArrayList<>();
                    map.put(paletteId, dto);
                }
                WarehouseUnitDto unitDto = new WarehouseUnitDto();
                unitDto.id = rs.getLong("unitId");
                unitDto.uniqId = rs.getLong("uniqId");
                unitDto.warehousePaletteId = paletteId;
                unitDto.harvestPaletteId = rs.getLong("harvestPaletteId");
                unitDto.pickerId = rs.getLong("pickerId");
                unitDto.pickerName = rs.getString("pickerName");
                unitDto.pickerSurname = rs.getString("pickerSurname");
                unitDto.cycleId = rs.getLong("cycleId");
                unitDto.chamberName = rs.getString("chamberName");
                unitDto.typeName = rs.getString("typeName");
                unitDto.typeWeight = rs.getDouble("typeWeight");
                unitDto.harvestTime = rs.getDate("harvestTime");
                unitDto.warehouseUnitStatus = WarehouseUnitStatus.values()[rs.getInt("warehouseStatus")];
                if (unitDto.uniqId > 0) {
                    dto.warehouseUnits.add(unitDto);
                }
            }
            return new ArrayList<>(map.values());
        }
    }

    public CreatedWarehousePaletteDto getCreatedWarehousePalette(long paletteId) {
        return jdbcTemplate.query("select p.id                 as warehousePaletteId, " +
                        "       p.palette_type_id    as paletteTypeId, " +
                        "       u.id                 as unitId, " +
                        "       u.local_type_id      as typeId, " +
                        "       u.uniq_id            as uniqId, " +
                        "       u.harvest_palette_id as harvestPaletteId, " +
                        "       u.picker_id          as pickerId, " +
                        "       l.imie               as pickerName, " +
                        "       l.nazwisko           as pickerSurname, " +
                        "       u.cycle_id           as cycleId, " +
                        "       h.name               as chamberName, " +
                        "       r.name               as typeName, " +
                        "       r.waga               as typeWeight, " +
                        "       u.harvest_time       as harvestTime, " +
                        "       u.status             as warehouseStatus " +
                        "from east_mushrooms_warehouse_palette p " +
                        "       left join east_mushrooms_warehouse_unit u on u.palette_id = p.id " +
                        "       left join ludzie l on l.id = u.picker_id " +
                        "       left join cykle c on c.id = u.cycle_id " +
                        "       left join hala h on h.id = c.hala_id " +
                        "       left join rodzaj r on r.id = u.local_type_id " +
                        "where p.id = ?",
                new Object[]{paletteId}, new CreatedWarehousePaletteResultSetExtractor());
    }

    private class CreatedWarehousePaletteResultSetExtractor implements ResultSetExtractor<CreatedWarehousePaletteDto> {
        @Override
        public CreatedWarehousePaletteDto extractData(ResultSet rs) throws SQLException {
            CreatedWarehousePaletteDto dto = new CreatedWarehousePaletteDto();
            dto.warehouseUnits = new ArrayList<>();
            while (rs.next()) {
                dto.paletteId = rs.getLong("warehousePaletteId");
                dto.paletteTypeId = rs.getInt("paletteTypeId");
                WarehouseUnitDto unitDto = new WarehouseUnitDto();
                unitDto.id = rs.getLong("unitId");
                unitDto.uniqId = rs.getLong("uniqId");
                unitDto.warehousePaletteId = dto.paletteId;
                unitDto.harvestPaletteId = rs.getLong("harvestPaletteId");
                unitDto.pickerId = rs.getLong("pickerId");
                unitDto.pickerName = rs.getString("pickerName");
                unitDto.pickerSurname = rs.getString("pickerSurname");
                unitDto.cycleId = rs.getLong("cycleId");
                unitDto.chamberName = rs.getString("chamberName");
                unitDto.typeId = rs.getLong("typeId");
                unitDto.typeName = rs.getString("typeName");
                unitDto.typeWeight = rs.getDouble("typeWeight");
                unitDto.harvestTime = rs.getDate("harvestTime");
                unitDto.warehouseUnitStatus = WarehouseUnitStatus.values()[rs.getInt("warehouseStatus")];
                if (unitDto.uniqId > 0) {
                    dto.warehouseUnits.add(unitDto);
                }
            }
            return dto;
        }
    }

    public List<WarehousePaletteDto> getPalettesReadyToRelease() {
        return jdbcTemplate.query("select   " +
                "                  p.id                 as paletteId,   " +
                "                  p.palette_type_id    as paletteTypeId,   " +
                "                  u.local_type_id      as localTypeId,    " +
                "                  t.name               as localTypeName,    " +
                "                  t.waga               as localTypeWeight,    " +
                "                  rt.id                as remoteTypeId,    " +
                "                  rt.name              as remoteTypeName,    " +
                "                  rt.weight            as remoteTypeWeight,    " +
                "                  count(u.id)          as amount,    " +
                "                  p.status             as status   " +
                "                  from east_mushrooms_warehouse_palette p   " +
                "                  left join east_mushrooms_warehouse_unit u on u.palette_id = p.id   " +
                "                  left join rodzaj t on t.id = u.local_type_id    " +
                "                  left join skup_rodzaj rt on rt.id = t.skup_rodzaj_id " +
                "                  where p.status =  1   " +
                "                  group by p.id,u.local_type_id   " +
                "                  having count(u.id) > 0", new PalettesReadyToReleaseResultSetExtractor());
    }

    private class PalettesReadyToReleaseResultSetExtractor implements ResultSetExtractor<List<WarehousePaletteDto>> {
        @Override
        public List<WarehousePaletteDto> extractData(ResultSet rs) throws SQLException {
            List<WarehousePaletteDto> list = new ArrayList<>();
            while (rs.next()) {
                WarehousePaletteDto dto = new WarehousePaletteDto();
                dto.paletteId = rs.getLong("paletteId");
                dto.paletteTypeId = rs.getInt("paletteTypeId");
                dto.localTypeId = rs.getLong("localTypeId");
                dto.localTypeName = rs.getString("localTypeName");
                dto.localTypeWeight = rs.getDouble("localTypeWeight");
                dto.remoteTypeId = rs.getLong("remoteTypeId");
                dto.remoteTypeName = rs.getString("remoteTypeName");
                dto.remoteTypeWeight = rs.getDouble("remoteTypeWeight");
                dto.amount = rs.getInt("amount");
                dto.warehousePaletteStatus = WarehousePaletteStatus.values()[rs.getInt("status")];
                list.add(dto);
            }
            return list;
        }
    }

    public WarehousePaletteDto getPaletteReadyToRelease(long paletteId) {
        return jdbcTemplate.query("select u.palette_id    as paletteId, " +
                "       emwp.palette_type_id as paletteTypeId, " +
                "       u.local_type_id as localTypeId, " +
                "       t.name          as localTypeName, " +
                "       t.waga          as localTypeWeight, " +
                "       rt.id           as remoteTypeId, " +
                "       rt.name         as remoteTypeName, " +
                "       rt.weight       as remoteTypeWeight, " +
                "       count(*)        as amount, " +
                "       u.status        as status " +
                "from east_mushrooms_warehouse_unit u " +
                "         left join rodzaj t on t.id = u.local_type_id " +
                "         left join skup_rodzaj rt on rt.id = t.skup_rodzaj_id " +
                "         left join east_mushrooms_warehouse_palette emwp on u.palette_id = emwp.id " +
                "where u.status = 1 " +
                "  and u.palette_id = ? " +
                "group by u.palette_id, u.local_type_id; ", new Object[]{paletteId}, new PaletteReadyToReleaseResultSetExtractor());
    }

    private class PaletteReadyToReleaseResultSetExtractor implements ResultSetExtractor<WarehousePaletteDto> {
        @Override
        public WarehousePaletteDto extractData(ResultSet rs) throws SQLException {
            WarehousePaletteDto dto = new WarehousePaletteDto();
            while (rs.next()) {
                dto.paletteId = rs.getLong("paletteId");
                dto.paletteTypeId = rs.getInt("paletteTypeId");
                dto.localTypeId = rs.getLong("localTypeId");
                dto.localTypeName = rs.getString("localTypeName");
                dto.localTypeWeight = rs.getDouble("localTypeWeight");
                dto.remoteTypeId = rs.getLong("remoteTypeId");
                dto.remoteTypeName = rs.getString("remoteTypeName");
                dto.remoteTypeWeight = rs.getDouble("remoteTypeWeight");
                dto.amount = rs.getInt("amount");
                dto.warehousePaletteStatus = WarehousePaletteStatus.values()[rs.getInt("status")];
            }
            return dto;
        }
    }

    public List<ShipmentHeaderDto> getShipmentHeaders(Date startDate, Date endDate) {
        return jdbcTemplate.query("select " +
                " s.id                             as shipmentId, " +
                " s.monthly_no                     as shipmentMonthlyNo, " +
                " s.creation_date                  as shipmentDate, " +
                " s.customer_name                  as shipmentCustomerName, " +
                " wz.id                            as wzDocumentId, " +
                " wz.monthly_no                    as wzMonthlyNo, " +
                " wz.date                          as wzDate, " +
                " wz.customer_name                 as wzCustomerName, " +
                " wz.company_id                    as companyId, " +
                " wz.company_name                  as companyName, " +
                " ROUND(sum(wu.local_type_weight) * 1000) as wzTotalAmount " +
                " from east_mushrooms_wz_unit wu " +
                " left join east_mushrooms_wz_document wz on wz.id = wu.wz_id " +
                " left join east_mushrooms_shipment s on s.id = wz.shipment_id " +
                " where s.creation_date >= ? " +
                "  and s.creation_date <= ? " +
                " group by wz.id ", new Object[]{startDate, endDate}, new ShipmentHeadersResultSetExtractor());
    }

    private class ShipmentHeadersResultSetExtractor implements ResultSetExtractor<List<ShipmentHeaderDto>> {
        @Override
        public List<ShipmentHeaderDto> extractData(ResultSet rs) throws SQLException {
            Map<Long, ShipmentHeaderDto> map = new HashMap<>();
            ShipmentHeaderDto shipmentHeaderDto;
            while (rs.next()) {
                long shipmentId = rs.getLong("shipmentId");
                if (map.containsKey(shipmentId)) {
                    shipmentHeaderDto = map.get(shipmentId);
                } else {
                    shipmentHeaderDto = new ShipmentHeaderDto();
                    shipmentHeaderDto.id = shipmentId;
                    shipmentHeaderDto.customerName = rs.getString("shipmentCustomerName");
                    shipmentHeaderDto.date = rs.getDate("shipmentDate");
                    shipmentHeaderDto.nr = rs.getInt("shipmentMonthlyNo");
                    shipmentHeaderDto.wzHeaders = new ArrayList<>();
                    map.put(shipmentId, shipmentHeaderDto);
                }
                WzHeaderDto wzHeaderDto = new WzHeaderDto();
                wzHeaderDto.id = rs.getLong("wzDocumentId");
                wzHeaderDto.companyId = rs.getInt("companyId");
                wzHeaderDto.companyName = rs.getString("companyName");
                wzHeaderDto.customerName = rs.getString("wzCustomerName");
                wzHeaderDto.date = rs.getDate("wzDate");
                wzHeaderDto.nr = rs.getInt("wzMonthlyNo");
                wzHeaderDto.totalAmount = rs.getLong("wzTotalAmount");
                shipmentHeaderDto.wzHeaders.add(wzHeaderDto);
            }
            return new ArrayList<>(map.values());
        }
    }


    public ShipmentHeaderDto getShipmentHeader(long shipmentId) {
        return jdbcTemplate.query("select   + " +
                " s.id            as shipmentId, " +
                " s.monthly_no    as shipmentMonthlyNo, " +
                " s.creation_date as shipmentDate, " +
                " s.customer_name as shipmentCustomerName, " +
                " wz.id as wzDocumentId, " +
                " wz.monthly_no as wzMonthlyNo, " +
                " wz.date as wzDate, " +
                " wz.customer_name as wzCustomerName, " +
                " wz.company_id as companyId, " +
                " wz.company_name as companyName, " +
                " round(sum(wu.local_type_weight) * 1000)  as wzTotalAmount " +
                " from east_mushrooms_shipment s   " +
                " left join east_mushrooms_wz_document wz on s.id = wz.shipment_id " +
                " left join east_mushrooms_wz_unit wu on wu.wz_id = wz.id " +
                " left join rodzaj r on r.id = wu.local_type_id " +
                " where s.id = ? " +
                " group by wz.id", new Object[]{shipmentId}, new ShipmentHeaderResultSetExtractor());
    }

    private class ShipmentHeaderResultSetExtractor implements ResultSetExtractor<ShipmentHeaderDto> {

        @Override
        public ShipmentHeaderDto extractData(ResultSet rs) throws SQLException {
            ShipmentHeaderDto shipmentHeaderDto = new ShipmentHeaderDto();
            boolean isShipmentCreated = false;
            while (rs.next()) {
                if (!isShipmentCreated) {
                    shipmentHeaderDto.id = rs.getLong("shipmentId");
                    shipmentHeaderDto.customerName = rs.getString("shipmentCustomerName");
                    shipmentHeaderDto.date = rs.getDate("shipmentDate");
                    shipmentHeaderDto.nr = rs.getInt("shipmentMonthlyNo");
                    shipmentHeaderDto.wzHeaders = new ArrayList<>();
                    isShipmentCreated = true;
                }
                WzHeaderDto wzHeaderDto = new WzHeaderDto();
                wzHeaderDto.id = rs.getLong("wzDocumentId");
                wzHeaderDto.companyId = rs.getInt("companyId");
                wzHeaderDto.companyName = rs.getString("companyName");
                wzHeaderDto.customerName = rs.getString("wzCustomerName");
                wzHeaderDto.date = rs.getDate("wzDate");
                wzHeaderDto.nr = rs.getInt("wzMonthlyNo");
                wzHeaderDto.totalAmount = rs.getLong("wzTotalAmount");
                shipmentHeaderDto.wzHeaders.add(wzHeaderDto);
            }
            return shipmentHeaderDto;
        }
    }


    public WzDto getWz(long wzId) {
        return jdbcTemplate.query("select " +
                "       wz.id                   as wzId, " +
                "       wz.monthly_no           as wzNr, " +
                "       wz.company_name         as companyName, " +
                "       wz.company_city         as companyCity, " +
                "       wz.company_street       as companyStreet, " +
                "       wz.company_nip          as companyNip, " +
                "       wz.company_ggn          as companyGGN, " +
                "       wz.customer_name        as customerName, " +
                "       wz.date                 as date, " +
                "       wz.creator_login        as creatorLogin, " +
                "       wz.creator_name         as creatorName, " +
                "       wz.creator_surname      as creatorSurname, " +
                "       u.local_type_id        as localTypeId, " +
                "       u.local_type_name      as localTypeName, " +
                "       u.local_type_weight    as localTypeWeight, " +
                "       u.remote_type_id       as remoteTypeId, " +
                "       u.remote_type_name     as remoteTypeName, " +
                "       u.remote_type_weight   as remoteTypeWeight, " +
                "       count(*)               as amount " +
                "from east_mushrooms_wz_document wz " +
                "         left join east_mushrooms_wz_unit u on u.wz_id = wz.id " +
                "where wz.id = ? " +
                "group by u.local_type_id", new Object[]{wzId}, new WzDtoResultSetExtractor());
    }

    private class WzDtoResultSetExtractor implements ResultSetExtractor<WzDto> {
        @Override
        public WzDto extractData(ResultSet rs) throws SQLException {
            WzDto wzDto = new WzDto();
            while (rs.next()) {
                if (wzDto.wzId == null) {
                    wzDto.wzId = rs.getLong("wzId");
                    wzDto.wzNr = rs.getInt("wzNr");
                    wzDto.companyName = rs.getString("companyName");
                    wzDto.companyCity = rs.getString("companyCity");
                    wzDto.companyStreet = rs.getString("companyStreet");
                    wzDto.companyNip = rs.getString("companyNip");
                    wzDto.companyGGN = rs.getString("companyGGN");
                    wzDto.customerName = rs.getString("customerName");
                    wzDto.date = rs.getDate("date");
                    wzDto.creatorLogin = rs.getString("creatorLogin");
                    wzDto.creatorName = rs.getString("creatorName");
                    wzDto.creatorSurname = rs.getString("creatorSurname");
                    wzDto.types = new ArrayList<>();
                }
                WzTypeDto typeDto = new WzTypeDto();
                typeDto.localTypeId = rs.getLong("localTypeId");
                typeDto.localTypeName = rs.getString("localTypeName");
                typeDto.localTypeWeight = rs.getDouble("localTypeWeight");
                typeDto.remoteTypeId = rs.getLong("remoteTypeId");
                typeDto.remoteTypeName = rs.getString("remoteTypeName");
                typeDto.remoteTypeWeight = rs.getDouble("remoteTypeWeight");
                typeDto.amount = rs.getInt("amount");
                wzDto.types.add(typeDto);
            }
            return wzDto;
        }
    }

    public List<WarehouseStatusDto> getWarehouseStatus() {
        return jdbcTemplate.query("select " +
                "       w.local_type_id as typeId, " +
                "       r.name          as typeName, " +
                "       r.waga          as typeWeight, " +
                "       count(*)        as amount " +
                "from east_mushrooms_warehouse_unit w, " +
                "     rodzaj r " +
                "where w.status = 1 " +
                "  and r.id = w.local_type_id " +
                "group by w.local_type_id", new WarehouseStatusResultSetExtractor());
    }

    private class WarehouseStatusResultSetExtractor implements ResultSetExtractor<List<WarehouseStatusDto>> {
        @Override
        public List<WarehouseStatusDto> extractData(ResultSet rs) throws SQLException {
            List<WarehouseStatusDto> list = new ArrayList<>();
            while (rs.next()) {
                WarehouseStatusDto dto = new WarehouseStatusDto();
                dto.typeId = rs.getLong("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getDouble("typeWeight");
                dto.amount = rs.getLong("amount");
                list.add(dto);
            }
            return list;
        }
    }

    public WarehouseUnitDto findWarehouseUnit(int uniqId, int pickerId) {
        return jdbcTemplate.query(" select " +
                "       wu.id            as id, " +
                "       wu.uniq_id       as uniqId, " +
                "       wu.picker_id     as pickerId, " +
                "       l.imie           as pickerName, " +
                "       l.nazwisko       as pickerSurname, " +
                "       wu.local_type_id as typeId, " +
                "       r.name           as typeName, " +
                "       r.waga           as typeWeight, " +
                "       wu.harvest_time  as harvestTime, " +
                "       h.name           as chamberName, " +
                "       wu.status        as status " +
                " from east_mushrooms_warehouse_unit wu " +
                "    left join ludzie l on wu.picker_id = l.id " +
                "    left join rodzaj r on wu.local_type_id = r.id " +
                "    left join cykle c on wu.cycle_id = c.id " +
                "    left join hala h on c.hala_id = h.id " +
                " where wu.picker_id = ? " +
                "  and wu.uniq_id = ?", new Object[]{pickerId, uniqId}, new WarehouseUnitResultSetExtractor());
    }

    private class WarehouseUnitResultSetExtractor implements ResultSetExtractor<WarehouseUnitDto> {
        @Override
        public WarehouseUnitDto extractData(ResultSet rs) throws SQLException {
            WarehouseUnitDto dto = new WarehouseUnitDto();
            while (rs.next()) {
                dto.id = rs.getLong("id");
                dto.uniqId = rs.getLong("uniqId");
                dto.pickerId = rs.getLong("pickerId");
                dto.pickerName = rs.getString("pickerName");
                dto.pickerSurname = rs.getString("pickerSurname");
                dto.typeId = rs.getLong("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getDouble("typeWeight");
                dto.harvestTime = rs.getDate("harvestTime");
                dto.chamberName = rs.getString("chamberName");
                dto.warehouseUnitStatus = WarehouseUnitStatus.values()[rs.getInt("status")];
            }
            return dto;
        }
    }

    public ProxyShipmentDto getShipmentForProxy(long shipmentId) {
        return jdbcTemplate.query("select s.id                       as shipmentId, " +
                "       s.monthly_no               as shipmentMonthlyNo, " +
                "       s.creation_date            as shipmentCreationDate, " +
                "       s.creation_date            as shipmentCreationDate, " +
                "       s.creator_id               as shipmentCreatorId, " +
                "       s.creator_login            as shipmentCreatorLogin, " +
                "       s.creator_name             as shipmentCreatorName, " +
                "       s.creator_surname          as shipmentCreatorSurname, " +
                "       s.customer_name            as shipmentCustomerName, " +
                "       s.customer_address         as shipmentCustomerAddress, " +
                "       cust.producer_group_id     as shipmentCustomerGroupId, " +
                "       w.id                       as wzId, " +
                "       w.monthly_no               as wzNumber, " +
                "       w.company_id               as companyId, " +
                "       w.company_name             as companyName, " +
                "       w.company_street           as companyStreet, " +
                "       w.company_city             as companyCity, " +
                "       w.company_nip              as companyNip, " +
                "       w.company_ggn              as companyGGN, " +
                "       w.date                     as wzCreationDate, " +
                "       w.creator_login            as creatorLogin, " +
                "       w.creator_name             as creatorName, " +
                "       w.creator_surname          as creatorSurname, " +
                "       w.customer_name            as customerName, " +
                "       u.local_type_id            as farmTypeId, " +
                "       u.local_type_name          as farmTypeName, " +
                "       u.local_type_weight * 1000 as farmTypeWeight, " +
                "       u.remote_type_id           as warehouseTypeId, " +
                "       u.remote_type_name         as warehouseTypeName, " +
                "       u.remote_type_weight       as warehouseTypeWeight, " +
                "       u.picker_id                as pickerId, " +
                "       u.uniq_id                  as uniqId, " +
                "       u.cycle_id                 as cycleId, " +
                "       u.chamber_name             as chamberName, " +
                "       u.harvest_date             as harvestDate, " +
                "       u.shipment_palette_id      as shipmentPaletteId, " +
                "       p.palette_type_id          as paletteTypeId " +
                "from east_mushrooms_shipment s, " +
                "     east_mushrooms_shipment_palette p, " +
                "     east_mushrooms_wz_document w, " +
                "     east_mushrooms_wz_unit u, " +
                "     skrz_cust cust " +
                "where s.id = p.shipment_id " +
                "  and u.shipment_palette_id = p.id " +
                "  and u.wz_id = w.id " +
                "  and cust.id = s.customer_id " +
                "  and s.id = ? ", new Object[]{shipmentId}, new ProxyShipmentDtoResultSetExtractor());
    }

    private class ProxyShipmentDtoResultSetExtractor implements ResultSetExtractor<ProxyShipmentDto> {
        @Override
        public ProxyShipmentDto extractData(ResultSet rs) throws SQLException {
            ProxyShipmentDto shipmentDto = null;
            Map<Long, ProxyWzDto> wzMap = new HashMap<>();
            while (rs.next()) {
                if (shipmentDto == null) {
                    shipmentDto = new ProxyShipmentDto();
                    shipmentDto.shipmentId = rs.getInt("shipmentId");
                    shipmentDto.shipmentMonthlyNo = rs.getInt("shipmentMonthlyNo");
                    shipmentDto.shipmentCreationDate = rs.getDate("shipmentCreationDate");
                    shipmentDto.shipmentCreatorId = rs.getInt("shipmentCreatorId");
                    shipmentDto.shipmentCreatorLogin = rs.getString("shipmentCreatorLogin");
                    shipmentDto.shipmentCreatorName = rs.getString("shipmentCreatorName");
                    shipmentDto.shipmentCreatorSurname = rs.getString("shipmentCreatorSurname");
                    shipmentDto.shipmentCustomerAddress = rs.getString("shipmentCustomerAddress");
                    shipmentDto.shipmentCustomerName = rs.getString("shipmentCustomerName");
                    shipmentDto.shipmentCustomerGroupId = rs.getInt("shipmentCustomerGroupId");
                    shipmentDto.wzDtoList = new ArrayList<>();
                }
                ProxyWzDto proxyWzDto;
                long wzId = rs.getLong("wzId");
                if (wzMap.containsKey(wzId)) {
                    proxyWzDto = wzMap.get(wzId);
                } else {
                    proxyWzDto = new ProxyWzDto();
                    proxyWzDto.wzId = rs.getInt("wzId");
                    proxyWzDto.wzNumber = rs.getInt("wzNumber");
                    proxyWzDto.companyCity = rs.getString("companyCity");
                    proxyWzDto.companyStreet = rs.getString("companyStreet");
                    proxyWzDto.companyGGN = rs.getString("companyGGN");
                    proxyWzDto.companyId = rs.getInt("companyId");
                    proxyWzDto.companyName = rs.getString("companyName");
                    proxyWzDto.companyNip = rs.getString("companyNip");
                    proxyWzDto.creationDate = rs.getDate("wzCreationDate");
                    proxyWzDto.creatorLogin = rs.getString("creatorLogin");
                    proxyWzDto.creatorName = rs.getString("creatorName");
                    proxyWzDto.creatorSurname = rs.getString("creatorSurname");
                    proxyWzDto.customerName = rs.getString("customerName");
                    proxyWzDto.units = new ArrayList<>();
                    wzMap.put(wzId, proxyWzDto);
                    shipmentDto.wzDtoList.add(proxyWzDto);
                }
                ProxyUnitDto unit = new ProxyUnitDto();
                unit.chamberName = rs.getString("chamberName");
                unit.cycleId = rs.getInt("cycleId");
                unit.farmTypeId = rs.getInt("farmTypeId");
                unit.farmTypeName = rs.getString("farmTypeName");
                unit.farmTypeWeight = rs.getInt("farmTypeWeight");
                unit.harvestDate = rs.getDate("harvestDate");
                unit.pickerId = rs.getInt("pickerId");
                unit.shipmentId = rs.getLong("shipmentId");
                unit.shipmentPaletteId = rs.getLong("shipmentPaletteId");
                unit.uniqId = rs.getInt("uniqId");
                unit.warehouseTypeId = rs.getInt("warehouseTypeId");
                unit.warehouseTypeName = rs.getString("warehouseTypeName");
                unit.warehouseTypeWeight = rs.getInt("warehouseTypeWeight");
                unit.paletteTypeId = rs.getInt("paletteTypeId");
                proxyWzDto.units.add(unit);
            }
            return shipmentDto;
        }
    }


    public List<ShipmentPaletteDto> getShipmentPalettes(long shipmentId) {
        return jdbcTemplate.query("select " +
                "       sp.id                           as shipmentPaletteId, " +
                "       sp.warehouse_palette_id         as warehousePaletteId, " +
                "       sp.shipment_id                  as shipmentId, " +
                "       r.id                            as typeId, " +
                "       pt.id                           as paletteTypeId, " +
                "       pt.name                         as paletteTypeName, " +
                "       r.name                          as typeName, " +
                "       r.waga * 1000                   as typeWeight, " +
                "       count(*)                        as totalAmount, " +
                "       round(count(*) * r.waga * 1000) as totalWeight " +
                "from east_mushrooms_shipment_palette sp " +
                "         left join east_mushrooms_wz_unit wu on wu.shipment_palette_id = sp.id " +
                "         left join rodzaj r on r.id = wu.local_type_id " +
                "         left join palette_type pt on pt.id = sp.palette_type_id " +
                "where sp.shipment_id = ? " +
                "group by sp.id, r.id;", new Object[]{shipmentId}, new ShipmentPalettesResultSetExtractor());
    }

    private class ShipmentPalettesResultSetExtractor implements ResultSetExtractor<List<ShipmentPaletteDto>> {
        @Override
        public List<ShipmentPaletteDto> extractData(ResultSet rs) throws SQLException {
            Map<Long, ShipmentPaletteDto> map = new HashMap<>();
            while (rs.next()) {
                long paletteId = rs.getLong("shipmentPaletteId");
                ShipmentPaletteDto paletteDto;
                if (map.containsKey(paletteId)) {
                    paletteDto = map.get(paletteId);
                } else {
                    paletteDto = new ShipmentPaletteDto();
                    paletteDto.paletteId = paletteId;
                    paletteDto.warehousePaletteId = rs.getLong("warehousePaletteId");
                    paletteDto.shipmentId = rs.getLong("shipmentId");
                    paletteDto.paletteTypeId = rs.getInt("paletteTypeId");
                    paletteDto.paletteTypeName = rs.getString("paletteTypeName");
                    paletteDto.types = new ArrayList<>();
                    map.put(paletteId, paletteDto);
                }
                ShipmentTypeDto shipmentTypeDto = new ShipmentTypeDto();
                shipmentTypeDto.typeId = rs.getLong("typeId");
                shipmentTypeDto.typeWeight = rs.getInt("typeWeight");
                shipmentTypeDto.typeName = rs.getString("typeName");
                shipmentTypeDto.amount = rs.getInt("totalAmount");
                shipmentTypeDto.weight = rs.getLong("totalWeight");
                paletteDto.types.add(shipmentTypeDto);
            }
            return new ArrayList<>(map.values());
        }
    }


}
