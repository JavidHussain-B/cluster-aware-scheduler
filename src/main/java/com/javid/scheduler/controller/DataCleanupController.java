package com.javid.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javid.scheduler.service.DataCleanupService;

@RestController
@RequestMapping("/scheduler")
public class DataCleanupController {

    @Autowired
    private DataCleanupService dataCleanupService;

    @PostMapping(value = "/data-cleanup")
    public void dataCleanup() {
        dataCleanupService.dataCleanup();
    }

}
