package com.ld.game.entity.impl;

public class Test {

    public static void main(String[] args) {
        float maxHp = 6;
        float currentHp = 3;

        float percent = (currentHp * 100.0f) / maxHp;

        System.out.println(percent / 100);
    }

}
