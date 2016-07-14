package io.summerx.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sso-client")
public class SSOClientController {

    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public ModelAndView sayHello(String name) {

        ModelAndView mav = new ModelAndView("demo/sayHello");
        mav.addObject("result", "Hello " + name);
        return mav;
    }
}
