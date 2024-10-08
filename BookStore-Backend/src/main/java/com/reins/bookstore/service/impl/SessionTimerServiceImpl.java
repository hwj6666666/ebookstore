package com.reins.bookstore.service.impl;

import com.reins.bookstore.service.SessionTimerService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionTimerServiceImpl implements SessionTimerService {

  private long startTime;

  @Override
  public void startTimer() {
    startTime = System.currentTimeMillis();
  }

  @Override
  public long stopTimer() {
    long endTime = System.currentTimeMillis();
    return endTime - startTime;
  }
}
