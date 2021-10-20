package com.fungisearch.fudriver.customer.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.customer.command.model.CustomerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 19.01.17.
 */
@Transactional
@Service
public class AddCustomerCommandHandler {

    private CustomerFactory customerFactory;

    @Autowired
    public AddCustomerCommandHandler(CustomerFactory customerFactory){
        this.customerFactory = customerFactory;
    }

    public CommandResult handle(AddCustomerCommand command){
        long customerId = customerFactory
                .getBuilder()
                .name(command.name)
                .address(command.address)
                .build()
                .create();
        return new CommandResult(customerId, CommandResult.Status.OK,"CustomerCreated");
    }
}
