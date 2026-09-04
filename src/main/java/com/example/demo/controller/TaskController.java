package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/taskcontroller") @CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskController {

}
