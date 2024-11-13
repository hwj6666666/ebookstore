package org.example.calculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {
  @GetMapping("/calculator")
  public double calculator(@RequestParam int price, @RequestParam int quantity) {
    return price * quantity;
  }
}
