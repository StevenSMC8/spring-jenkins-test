package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopController {

  @GetMapping
  public String showShopPage() {
    return "shop"; // This corresponds to src/main/resources/templates/shop.html
  }
}
