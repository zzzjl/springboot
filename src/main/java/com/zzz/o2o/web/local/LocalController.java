package com.zzz.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/local")
public class LocalController {
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    private String login(){
        return "/local/login";
    }
}
