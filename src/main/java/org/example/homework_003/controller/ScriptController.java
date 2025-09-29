package org.example.homework_003.controller;

import org.example.homework_003.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/script1")
public class ScriptController {
    @Autowired
    private ScriptService scriptService;

    @GetMapping("/run-script")
    public String runScript() {
        return scriptService.runScript();
    }
}
