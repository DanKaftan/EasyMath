package com.dan.kaftan.game.targil;

public class TargilAdd extends AbstractTargil {

    public TargilAdd() {
        super();
    }

    public TargilAdd(int firstnum, int secondNum, String operator) {
        super(firstnum, secondNum, operator);
    }

    @Override
    public int calc() {
        return firstnum + secondNum;
    }
}
