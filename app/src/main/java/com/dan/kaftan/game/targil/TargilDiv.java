package com.dan.kaftan.game.targil;

public class TargilDiv extends AbstractTargil{
    public TargilDiv() {
        super();
    }

    public TargilDiv(int firstnum, int secondNum, String operator) {
        super(firstnum, secondNum, operator);
    }
    @Override
    public int calc() {
        return firstnum / secondNum;
    }
}
