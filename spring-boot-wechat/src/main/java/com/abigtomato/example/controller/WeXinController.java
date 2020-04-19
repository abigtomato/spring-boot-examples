package com.abigtomato.example.controller;

import com.abigtomato.example.service.WeiXinAuthService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx")
@Slf4j
public class WeXinController {

    public WeiXinAuthService weiXinAuthService;

    @Autowired
    public WeXinController(WeiXinAuthService weiXinAuthService) {
        this.weiXinAuthService = weiXinAuthService;
    }

    @PostMapping("/wx")
    @ApiImplicitParam(value = "code", name = "")
    public String wxLogin(@RequestParam("code") String code) {
        return weiXinAuthService.checkLogin(code);
    }
}
