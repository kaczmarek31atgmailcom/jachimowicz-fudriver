package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.WzRepository;
import org.springframework.stereotype.Service;

@Service
public class WzDocumentFactory {
    private final WzRepository wzRepository;
    private final BeanValidator beanValidator;
    private final UserService userService;

    public WzDocumentFactory(WzRepository wzRepository, BeanValidator beanValidator, UserService userService) {
        this.wzRepository = wzRepository;
        this.beanValidator = beanValidator;
        this.userService = userService;
    }

    public WzDocument.WzDocumentBuilder getBuilder(){
        return new WzDocument.WzDocumentBuilder(wzRepository,beanValidator,userService);
    }
}
