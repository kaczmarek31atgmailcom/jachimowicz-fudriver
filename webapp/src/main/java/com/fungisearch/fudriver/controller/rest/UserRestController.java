package com.fungisearch.fudriver.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
public class
UserRestController {

    @RequestMapping("/rest/current-user")
    public Principal user(Principal user) {
        return user;
    }

}
