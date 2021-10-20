package com.fungisearch.fudriver.productionOrder.query.dao;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.productionOrder.query.dto.OrderProgressBarDto;
import com.fungisearch.fudriver.productionOrder.query.dto.ProductionOrderDto;
import com.fungisearch.fudriver.productionOrder.query.dto.ProductionOrderScanDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ProductionOrderDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductionOrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductionOrderDto> getOrders(Date startDate, Date endDate) {
        return jdbcTemplate.query("select    " +
                "                        o.warehouse_order_id as warehouseOrderId,   " +
                "                        o.creation_date      as creationDate,   " +
                "                        o.due_date           as dueDate,   " +
                "                        o.warehouse_type_id  as warehouseTypeId,   " +
                "                        sk.name              as warehouseTypeName,   " +
                "                        sk.weight            as warehouseTypeWeight,   " +
                "                        r.id                 as localTypeId,   " +
                "                        r.name               as localTypeName,   " +
                "                        r.waga * 1000        as localTypeWeight,   " +
                "                        o.due_amount         as dueAmount,   " +
                "                        o.delivered_amount   as deliveredAmount,   " +
                "                        o.description        as description   " +
                "                  from production_order o   " +
                "                          left join skup_rodzaj sk on sk.remote_id = o.warehouse_type_id   " +
                "                          left join rodzaj r on r.skup_rodzaj_id = sk.id " +
                "                  where status = 0  " +
                "                  and o.due_date between ? and ? ", new Object[]{DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate)}, new ProductionOrdersResultSetExtractor());
    }

    private class ProductionOrdersResultSetExtractor implements ResultSetExtractor<List<ProductionOrderDto>> {
        @Override
        public List<ProductionOrderDto> extractData(ResultSet rs) throws SQLException {
            List<ProductionOrderDto> list = new ArrayList<>();
            while (rs.next()) {
                ProductionOrderDto dto = new ProductionOrderDto();
                dto.warehouseOrderId = rs.getInt("warehouseOrderId");
                dto.creationDate = rs.getDate("creationDate");
                dto.dueDate = rs.getDate("dueDate");
                dto.warehouseTypeId = rs.getInt("warehouseTypeId");
                dto.warehouseTypeName = rs.getString("warehouseTypeName");
                dto.warehouseTypeWeight = rs.getInt("warehouseTypeWeight");
                dto.localTypeId = rs.getInt("localTypeId");
                dto.localTypeName = rs.getString("localTypeName");
                dto.localTypeWeight = rs.getInt("localTypeWeight");
                dto.dueAmount = rs.getInt("dueAmount");
                dto.deliveredAmount = rs.getInt("deliveredAmount");
                dto.description = rs.getString("description");
                list.add(dto);
            }
            return list;
        }
    }

    public List<OrderProgressBarDto> getProductionOrderStatus() {
        return jdbcTemplate.query("select   " +
                "                        o.warehouse_type_id as warehouseTypeId,   " +
                "                        r.id as typeId, " +
                "                        r.name             as typeName,   " +
                "                        r.waga             as typeWeight,   " +
                "                        round((select count(*) from east_mushrooms_warehouse_unit u where u.local_type_id = r.id and (status = 0 || status = 1)) * r.waga ,2) as warehouseWeight,   " +
                "                        round((select count(*) from wozek w where w.rodzaj_id = r.id) * r.waga,2) as scaleWeight,   " +
                "                        round((o.due_amount - o.delivered_amount) * r.waga,2) as dueAmount   " +
                "                 from production_order o   " +
                "                          left join skup_rodzaj sk on sk.remote_id = o.warehouse_type_id   " +
                "                          left join rodzaj r on r.skup_rodzaj_id = sk.id " +
                "                 where o.due_date = curdate() ", new ProductionOrderProgressBarResultSetExtractor());
    }

    private class ProductionOrderProgressBarResultSetExtractor implements ResultSetExtractor<List<OrderProgressBarDto>> {
        @Override
        public List<OrderProgressBarDto> extractData(ResultSet rs) throws SQLException {
            List<OrderProgressBarDto> list = new ArrayList<>();
            while (rs.next()) {
                OrderProgressBarDto dto = new OrderProgressBarDto();
                dto.warehouseTypeId = rs.getInt("warehouseTypeId");
                dto.typeId = rs.getInt("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getDouble("typeWeight");
                dto.order = rs.getDouble("dueAmount");
                dto.totalWeight = 0.0;
                dto.totalWeight += rs.getDouble("warehouseWeight");
                dto.totalWeight += rs.getDouble("scaleWeight");
                if(dto.order > dto.totalWeight) {
                    list.add(dto);
                }
            }
            return list;
        }
    }


    public List<ProductionOrderScanDto> getProductionOrdersForScanner(){
        return jdbcTemplate.query("select   " +
                "                        o.warehouse_type_id                                                                                                as warehouseTypeId,   " +
                "                        r.id                                                                                                               as localTypeId, " +
                "                        r.name                                                                                                             as localTypeName,   " +
                "                        round(r.waga * 1000)                                                                                               as localTypeWeight,   " +
                "                        o.due_date                                                                                                         as dueDate,   " +
                "                        (select count(*) from east_mushrooms_warehouse_unit u where u.local_type_id = r.id and (status = 0 || status = 1)) as warehouseAmount,   " +
                "                        (select count(*) from wozek w where w.rodzaj_id = r.id)                                                            as scaleAmount,   " +
                "                        o.due_amount - ifnull((select count(*)   " +
                "                                               from east_mushrooms_wz_unit u,   " +
                "                                                    east_mushrooms_wz_document wz   " +
                "                                               where u.local_type_id = r.id   " +
                "                                                 and u.wz_id = wz.id   " +
                "                                                 and wz.date = curdate()), 0)                                                              as dueAmount   " +
                "                 from production_order o   " +
                "                          left join skup_rodzaj sk on sk.remote_id = o.warehouse_type_id   " +
                "                          left join rodzaj r on r.skup_rodzaj_id = sk.id " +
                "                 where o.due_date = curdate() or o.due_date = curdate() + interval 1 Day", new ProductionOrderScanResultSetExtractor());
    }

    private class ProductionOrderScanResultSetExtractor implements ResultSetExtractor<List<ProductionOrderScanDto>> {
        @Override
        public List<ProductionOrderScanDto> extractData(ResultSet rs) throws SQLException {
            List<ProductionOrderScanDto> list = new ArrayList<>();
            while (rs.next()){
                ProductionOrderScanDto dto = new ProductionOrderScanDto();
                dto.warehouseTypeId = rs.getInt("warehouseTypeId");
                dto.localTypeName = rs.getString("localTypeName");
                dto.localTypeWeight = rs.getInt("localTypeWeight");
                dto.dueAmount = rs.getInt("dueAmount");
                dto.dueDate = rs.getDate("dueDate");
                dto.preparedAmount = 0;
                dto.preparedAmount += rs.getInt("warehouseAmount");
                dto.preparedAmount += rs.getInt("scaleAmount");
                list.add(dto);
            }
            return list;
        }
    }
}
