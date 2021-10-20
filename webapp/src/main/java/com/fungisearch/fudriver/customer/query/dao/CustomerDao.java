package com.fungisearch.fudriver.customer.query.dao;

import com.fungisearch.fudriver.customer.query.dto.CustomerDto;
import com.fungisearch.fudriver.customer.query.dto.OrderCustomerDto;
import com.fungisearch.fudriver.customer.query.dto.OutgoCustomerDto;

import java.util.List;

/**
 * Created by marcin on 25.04.16.
 */
public interface CustomerDao {
    List<OutgoCustomerDto> findOutgoCustomers();
    List<OrderCustomerDto> findOrderCustomers();
    List<CustomerDto> findActiveCustomers();
}
