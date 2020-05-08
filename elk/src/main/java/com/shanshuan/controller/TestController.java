package com.shanshuan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by wangzifeng on 2020/5/7.
 */
@RestController
public class TestController {
    private static final Logger LOG = Logger.getLogger(TestController.class.getName());

    @RequestMapping(value = "/elkdemo")
    public String helloWorld() {
        String response = "Hello user ! " + new Date();
        LOG.log(Level.INFO, "/elkdemo - &gt; " + response);
        return response;
    }
}
