package kr.co.pinpick.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pinpick.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "유저 관련 API")
public class UserController {

    private final UserService userService;
}
