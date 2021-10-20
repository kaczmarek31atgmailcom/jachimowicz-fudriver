package com.fungisearch.fudriver.customer.query.dao;

import com.fungisearch.fudriver.customer.command.model.ProducerGroup;
import com.fungisearch.fudriver.customer.query.dto.CustomerDto;
import com.fungisearch.fudriver.customer.query.dto.OrderCustomerDto;
import com.fungisearch.fudriver.customer.query.dto.OutgoCustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 25.04.16.
 */
@Repository
public class CustomerJdbcDao implements CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<OutgoCustomerDto> findOutgoCustomers() {
        return jdbcTemplate.query("select id as id, name as name, address as address from skrz_cust where active = 1 order by name", new OutgoResultSetExtractor());
    }


    private class OutgoResultSetExtractor implements ResultSetExtractor<List<OutgoCustomerDto>> {
        @Override
        public List<OutgoCustomerDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<OutgoCustomerDto> list = new ArrayList<OutgoCustomerDto>();
            while (rs.next()) {
                OutgoCustomerDto dto = new OutgoCustomerDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                dto.address = rs.getString("address");
                list.add(dto);
            }
            return list;
        }
    }

    @Override
    public List<OrderCustomerDto> findOrderCustomers() {
        return jdbcTemplate.query("select id as id, name as name, address as address from skrz_cust where active = 1 order by name", new OrderResultSetExtractor());
    }

    private class OrderResultSetExtractor implements ResultSetExtractor<List<OrderCustomerDto>>{

        @Override
        public List<OrderCustomerDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<OrderCustomerDto> result = new ArrayList<OrderCustomerDto>();
            while(rs.next()){
                OrderCustomerDto dto = new OrderCustomerDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                dto.address = rs.getString("address");
                result.add(dto);
            }
        return result;
        }
    }

    @Override
    public List<CustomerDto> findActiveCustomers() {
        return jdbcTemplate.query("select id as id, name as name, address as address , producer_group_id as producerGroup from skrz_cust where active = 1 order by name", new ActiveCustomersResultSetExtractor());
    }

    private static class ActiveCustomersResultSetExtractor implements ResultSetExtractor<List<CustomerDto>>{

        @Override
        public List<CustomerDto> extractData(ResultSet rs) throws SQLException {
            List<CustomerDto> list = new ArrayList();
            while (rs.next()) {
                CustomerDto dto = new CustomerDto();
                dto.id = rs.getLong("id");
                dto.name = rs.getString("name");
                dto.address = rs.getString("address");
                dto.producerGroup = ProducerGroup.values()[rs.getInt("producerGroup")];
                list.add(dto);
            }
            return list;
        }
    }

}
