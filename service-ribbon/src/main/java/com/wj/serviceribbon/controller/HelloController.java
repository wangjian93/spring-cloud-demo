package com.wj.serviceribbon.controller;

import com.wj.serviceribbon.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjian
 * @date 2018/12/12
 */
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("/hi")
    public String Hello(@RequestParam(value = "name", defaultValue = "Ti") String name) {
        return helloService.helloService(name);
    }
}
