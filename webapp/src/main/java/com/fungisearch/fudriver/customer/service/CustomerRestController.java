package com.fungisearch.fudriver.customer.service;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.customer.command.*;
import com.fungisearch.fudriver.customer.query.dao.CustomerDao;
import com.fungisearch.fudriver.customer.query.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by marcin on 19.01.17.
 */
@RestController
public class CustomerRestController {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private AddCustomerCommandHandler addCustomerCommandHandler;
    @Autowired
    private UpdateCustomerCommandHandler updateCustomerCommandHandler;
    @Autowired
    private DeleteCustomerCommandHandler deleteCustomerCommandHandler;

    @RequestMapping(value = "/rest/customer", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public CommandResult createCustomer(@RequestBody AddCustomerCommand command){
        return addCustomerCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/customer", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public CommandResult updateCustomer(@RequestBody UpdateCustomerCommand command){
        return updateCustomerCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/customer/{customerId}", method = RequestMethod.DELETE, produces = "application/json; charset=UTF-8")
    public CommandResult deleteCustomer(@PathVariable(name = "customerId") long customerId){
        return deleteCustomerCommandHandler.handle(customerId);
    }

    @RequestMapping(value = "/rest/customer/active", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<CustomerDto> findActiveCustomers(){
        return customerDao.findActiveCustomers();
    }

}
