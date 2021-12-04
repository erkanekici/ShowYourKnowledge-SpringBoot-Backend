package com.controller.api;

import com.config.EnvironmentConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(value="*")
@RequestMapping(path = "/")
public class HealthCheckController {

    @GetMapping
    public String checkApp(@RequestParam(name="user", required=false, defaultValue="guest") String user, Model model)
    {
        model.addAttribute("user",user);
        model.addAttribute("status","RUNNING");
        model.addAttribute("version", EnvironmentConfig.getProjectVersion());
        return "healthCheck";
    }

    @GetMapping(value = "/test")
    @ResponseBody
    public String test()
    {
        return "OK";
    }

}