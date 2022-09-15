package com.game.domain;

import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.Calendar;
import java.util.Date;

public class Player {
    private Long id;
    private String name; //до 12 знаков
    private String title; //до 30 знаков
    private Race race;
    private Profession profession;
    private Integer experience; //от 0 до 10,000,000
    private Integer level;
    private Integer untilNextLevel;
    private Date birthday;
    private Boolean banned;
    private static Long countPlayers = 0L;

    public Player(String name, String title, Race race, Profession profession) {
        if (name.length() > 12) {
            name = name.substring(0, 12);
        }
        if (title.length() > 30) {
            title = title.substring(0, 30);
        }
        this.id = ++countPlayers;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = 0;
        this.level = 0;
        this.untilNextLevel = expToNextLvl(this.level, this.experience);

        this.banned = false;
    }

    private Integer expToNextLvl(Integer lvl, Integer exp) {
        return 50 * (lvl + 1) * (lvl + 2) - exp;
    }


}

