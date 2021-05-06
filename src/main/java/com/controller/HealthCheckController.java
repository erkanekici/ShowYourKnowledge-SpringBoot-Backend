package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(value="*")
public class HealthCheckController {

    @RequestMapping(path = "/health", method = RequestMethod.GET)
    @CrossOrigin(value="*")
    public String checkApp(Model model)
    {
        model.addAttribute("status","RUNNING v1.0.4");
        return "healthCheck";
    }

}