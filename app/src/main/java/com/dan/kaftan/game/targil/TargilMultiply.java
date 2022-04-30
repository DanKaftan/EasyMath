package com.dan.kaftan.game.targil;

public class TargilMultiply extends AbstractTargil {

    public TargilMultiply() {
        super();
    }

    public TargilMultiply(int firstnum, int secondNum, String operator) {
        super(firstnum, secondNum, operator);
    }
    @Override
    public int calc() {
        return firstnum * secondNum;
    }
}
