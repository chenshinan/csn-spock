package com.chenshinan.spock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shinan.chen
 * @date 2018/9/24
 */
@RestController
@RequestMapping(value = "spock")
public class SpockController {

    @GetMapping(value = "/queryById/{id}")
    public ResponseEntity<String> queryById(@PathVariable Long id) {
        return new ResponseEntity<>(id.toString(), HttpStatus.OK);
    }
}
