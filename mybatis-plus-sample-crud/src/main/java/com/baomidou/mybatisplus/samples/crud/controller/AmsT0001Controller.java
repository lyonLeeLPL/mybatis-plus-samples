package com.baomidou.mybatisplus.samples.crud.controller;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-03-29
 */
@Controller
@RequestMapping("/amsT0001")
public class AmsT0001Controller {

    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        return "home";
    }
}
