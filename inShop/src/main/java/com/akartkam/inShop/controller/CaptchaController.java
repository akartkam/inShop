package com.akartkam.inShop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akartkam.inShop.util.Captcha;

@Controller
public class CaptchaController {
    @RequestMapping(value = "/captcha.jpg", method = RequestMethod.GET) 
    public @ResponseBody byte[] captcha(@RequestParam("width") Integer width, 
            @RequestParam("height") Integer height, HttpSession session) {
 
        Captcha captcha = Captcha.newCaptcha(width, height);
        session.setAttribute("captchaString", captcha.getCaptchaString());
 
        return captcha.getCaptchaJpegBytes();
    }
}
