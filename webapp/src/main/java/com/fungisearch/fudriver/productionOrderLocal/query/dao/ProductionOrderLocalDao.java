package com.fungisearch.fudriver.productionOrderLocal.query.dao;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.productionOrderLocal.query.dto.ProductionOrderLocalDto;
import com.fungisearch.fudriver.productionOrderLocal.query.dto.ProductionOrderLocalScanDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ProductionOrderLocalDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductionOrderLocalDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductionOrderLocalDto> getOrders(Date startDate, Date endDate){
        return jdbcTemplate.query("select " +
                "r.id as typeId, " +
                "r.name as typeName, " +
                "r.waga * 1000 as typeWeight, " +
                "ifnull(o.amount,0) as amount, " +
                "o.due_date as dueDate " +
                "from rodzaj r " +
                " left join production_order_local o on o.type_id = r.id and o.due_date between ? and ? " +
                "where r.archiwalne = 0", new Object[]{DateUtils.getStartOfDay(startDate),DateUtils.getEndOfDay(endDate)}, new ProductionOrdersLocalResultSetExtractor());
    }

    private static class ProductionOrdersLocalResultSetExtractor implements ResultSetExtractor<List<ProductionOrderLocalDto>>{

        @Override
        public List<ProductionOrderLocalDto> extractData(ResultSet rs) throws SQLException {
            List<ProductionOrderLocalDto> list = new ArrayList<>();
            while (rs.next()){
                ProductionOrderLocalDto dto = new ProductionOrderLocalDto();
                dto.typeId = rs.getInt("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getInt("typeWeight");
                dto.amount = rs.getInt("amount");
                dto.dueDate = rs.getDate("dueDate");
                list.add(dto);
            }
            return list;
        }
    }

    public ProductionOrderLocalDto getOrder(Date day, int typeId) {
        return jdbcTemplate.query("select " +
                "r.id as typeId, " +
                "r.name as typeName, " +
                "r.waga * 1000 as typeWeight, " +
                "ifnull(o.amount,0) as amount, " +
                "o.due_date as dueDate " +
                "from rodzaj r " +
                " left join production_order_local o on o.type_id = r.id and o.due_date = ? " +
                " where r.id = ? ", new Object[]{DateUtils.getStartOfDay(day),typeId}, new ProductionOrderLocalResultSetExtractor());
    }


    private class ProductionOrderLocalResultSetExtractor implements ResultSetExtractor<ProductionOrderLocalDto> {
        @Override
        public ProductionOrderLocalDto extractData(ResultSet rs) throws SQLException {
            ProductionOrderLocalDto dto = new ProductionOrderLocalDto();
            while (rs.next()){
                dto.typeId = rs.getInt("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getInt("typeWeight");
                dto.amount = rs.getInt("amount");
                dto.dueDate = rs.getDate("dueDate");
            }
            return dto;
        }
    }
    
    public List<ProductionOrderLocalScanDto> getProductionOrdersForScanner(){
        return jdbcTemplate.query("select o.type_id                                                                                    as typeId, " +
                "       r.name                                                                                                             as typeName, " +
                "       round(r.waga * 1000)                                                                                               as typeWeight, " +
                "       o.due_date                                                                                                         as dueDate, " +
                "       (select count(*) from east_mushrooms_warehouse_unit u where u.local_type_id = r.id and (status = 0 || status = 1)) as warehouseAmount, " +
                "       (select count(*) from wozek w where w.rodzaj_id = r.id)                                                            as scaleAmount, " +
                "       o.amount - ifnull((select count(*) " +
                "                          from east_mushrooms_wz_unit u, " +
                "                               east_mushrooms_wz_document wz " +
                "                          where u.local_type_id = r.id " +
                "                            and u.wz_id = wz.id " +
                "                            and wz.date = curdate()), 0)                                                                  as dueAmount " +
                "from production_order_local o " +
                "         left join rodzaj r on r.id = o.type_id " +
                "where o.due_date = curdate() " +
                "   or o.due_date = curdate() + interval 1 Day ", new ProductionOrdersForScannerResultSetExtractor());
    }

    private class ProductionOrdersForScannerResultSetExtractor implements ResultSetExtractor<List<ProductionOrderLocalScanDto>> {
        @Override
        public List<ProductionOrderLocalScanDto> extractData(ResultSet rs) throws SQLException {
            List<ProductionOrderLocalScanDto> list = new ArrayList<>();
            while (rs.next()){
                ProductionOrderLocalScanDto dto = new ProductionOrderLocalScanDto();
                dto.typeId = rs.getInt("typeId");
                dto.typeName = rs.getString("typeName");
                dto.typeWeight = rs.getInt("typeWeight");
                dto.preparedAmount = rs.getInt("scaleAmount");
                dto.preparedAmount += rs.getInt("warehouseAmount");
                dto.dueAmount = rs.getInt("dueAmount");
                dto.dueDate = rs.getDate("dueDate");
                list.add(dto);
            }
        return list;
        }
    }
}
