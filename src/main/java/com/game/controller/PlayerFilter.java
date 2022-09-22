package com.game.controller;

import com.game.entity.Profession;
import com.game.entity.Race;

public class PlayerFilter {

  public static final String NAME = "name";
  public static final String TITLE = "title";
  public static final String IS_BANNED = "banned";
  public static final String BIRTHDAY_AFTER = "after";
  public static final String BIRTHDAY_BEFORE = "before";
  public static final String RACE = "race";
  public static final String PROFESSION = "profession";
  public static final String EXPERIENCE_AFTER = "minExperience";
  public static final String EXPERIENCE_BEFORE = "maxExperience";
  public static final String LEVEL_AFTER = "minLevel";
  public static final String LEVEL_BEFORE = "maxLevel";


  private String name;
  private String title;
  private Boolean banned;
  private Long birthdayAfter;
  private Long birthdayBefore;
  private Race race;
  private Profession profession;
  private Integer experienceAfter;
  private Integer experienceBefore;
  private Integer levelAfter;
  private Integer levelBefore;

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

  public Profession getProfession() {
    return profession;
  }

  public void setProfession(Profession profession) {
    this.profession = profession;
  }

  public Integer getExperienceAfter() {
    return experienceAfter;
  }

  public void setExperienceAfter(Integer experienceAfter) {
    this.experienceAfter = experienceAfter;
  }

  public Integer getExperienceBefore() {
    return experienceBefore;
  }

  public void setExperienceBefore(Integer experienceBefore) {
    this.experienceBefore = experienceBefore;
  }

  public Integer getLevelAfter() {
    return levelAfter;
  }

  public void setLevelAfter(Integer levelAfter) {
    this.levelAfter = levelAfter;
  }

  public Integer getLevelBefore() {
    return levelBefore;
  }

  public void setLevelBefore(Integer levelBefore) {
    this.levelBefore = levelBefore;
  }
}
