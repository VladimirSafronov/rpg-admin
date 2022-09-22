package com.game.controller;

import com.game.service.exceptions.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerAspect {

  @ResponseBody
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  void serviceUnavailableHandler(IllegalArgumentException e) {
    System.out.println(e.getMessage());
  }

  @ResponseBody
  @ExceptionHandler(PlayerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  void serviceUnavailableHandler(PlayerNotFoundException e) {
    System.out.println(e.getMessage());
  }

}
