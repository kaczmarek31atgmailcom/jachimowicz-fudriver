package com.fungisearch.fudriver.customer.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.customer.command.model.Customer;
import com.fungisearch.fudriver.customer.command.model.CustomerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 20.01.17.
 */
@Service
@Transactional
public class UpdateCustomerCommandHandler {

    private CustomerFactory customerFactory;

    @Autowired
    public UpdateCustomerCommandHandler(CustomerFactory customerFactory){
        this.customerFactory = customerFactory;
    }

    public CommandResult handle(UpdateCustomerCommand command){
        customerFactory.find(command.id)
                .edit(new Customer.Edit()
                .name(command.name)
                .address(command.address));
        return new CommandResult(command.id, CommandResult.Status.OK,"CustomerUpdated");
    }
}
