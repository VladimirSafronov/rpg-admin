package com.game.controller;

import com.game.entity.Race;

public class PlayerFilter {

  public static final String NAME = "name";
  public static final String TITLE = "title";
  public static final String IS_BANNED = "banned";
  public static final String BIRTHDAY_AFTER = "after";
  public static final String BIRTHDAY_BEFORE = "before";
  public static final String RACE = "race";

  private String name;
  private String title;
  private Boolean banned;
  private Long birthdayAfter;
  private Long birthdayBefore;
  private Race race;

  public Boolean isBanned() {
    return banned;
  }

  public void setBanned(Boolean banned) {
    this.banned = banned;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getBirthdayAfter() {
    return birthdayAfter;
  }

  public void setBirthdayAfter(Long birthdayAfter) {
    this.birthdayAfter = birthdayAfter;
  }

  public Long getBirthdayBefore() {
    return birthdayBefore;
  }

  public void setBirthdayBefore(Long birthdayBefore) {
    this.birthdayBefore = birthdayBefore;
  }

  public Race getRace() {
    return race;
  }

  public void setRace(Race race) {
    this.race = race;
  }
}
