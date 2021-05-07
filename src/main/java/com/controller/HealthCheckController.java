package com.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(value="*")
public class HealthCheckController {

    @Value("${pom.version}")
    private String pomVersion;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @CrossOrigin(value="*")
    public String checkApp(@RequestParam(name="user", required=false, defaultValue="guest") String user, Model model)
    {
        model.addAttribute("user",user);
        model.addAttribute("status","RUNNING");
        model.addAttribute("version",pomVersion);
        return "healthCheck";
    }

}