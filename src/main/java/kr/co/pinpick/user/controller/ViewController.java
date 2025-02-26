package kr.co.pinpick.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    @GetMapping("/user/login")
    public String loginForm(Model model) {
        model.addAttribute("KAKAO_CLIENT_ID", KAKAO_CLIENT_ID);
        model.addAttribute("KAKAO_REDIRECT_URI", KAKAO_REDIRECT_URI);
        model.addAttribute("GOOGLE_CLIENT_ID", GOOGLE_CLIENT_ID);
        model.addAttribute("GOOGLE_REDIRECT_URI", GOOGLE_REDIRECT_URI);
        return "index";
    }
}
