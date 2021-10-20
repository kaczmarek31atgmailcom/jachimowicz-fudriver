package com.fungisearch.fudriver.customer.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.customer.command.model.CustomerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 20.01.17.
 */
@Service
@Transactional
public class DeleteCustomerCommandHandler {
    private CustomerFactory customerFactory;

    @Autowired
    public DeleteCustomerCommandHandler(CustomerFactory customerFactory){
        this.customerFactory = customerFactory;
    }

    public CommandResult handle(long id){
        customerFactory
                .find(id)
                .delete();
        return new CommandResult(id, CommandResult.Status.OK,"CustomerDeleted");
    }


}
