/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fungisearch.fudriver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 *
 * @author marcin
 */
@Controller
public class LeaderReportController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @RequestMapping( value = "/leaderReport", method = RequestMethod.GET)
    public String showLeaderReport(Model model){
        return "leader_report.jsp";
    }

    @RequestMapping(value = "/collectedByChamber", method = RequestMethod.GET)
    public String showCollectedByChamber(){
        return "collectedByChamber.jsp";
    }
}
