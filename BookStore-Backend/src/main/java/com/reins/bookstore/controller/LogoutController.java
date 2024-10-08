package com.reins.bookstore.controller;

import com.reins.bookstore.constants.Messages;
import com.reins.bookstore.models.ApiResponseBase;
import com.reins.bookstore.service.SessionTimerService;
import com.reins.bookstore.utils.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
@Tag(name = "Logout", description = "用户登出相关 API")
public class LogoutController {
  @Autowired SessionTimerService sessionTimerService;

  @PutMapping
  @Operation(summary = "登出")
  public ResponseEntity<ApiResponseBase> logout() {
    long time = sessionTimerService.stopTimer();
    SessionUtils.removeSession();
    return ResponseEntity.ok(ApiResponseBase.succeed(Messages.LOGOUT_SUCCEED, time));
  }
}
