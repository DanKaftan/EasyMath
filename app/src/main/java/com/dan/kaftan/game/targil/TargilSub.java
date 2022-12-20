package com.dan.kaftan.game.targil;

public class TargilSub extends AbstractTargil{
    public TargilSub() {
        super();
    }

    public TargilSub(int firstnum, int secondNum, String operator) {
        super(firstnum, secondNum, operator);
    }
    @Override
    public int calc() {
        return firstnum - secondNum;
    }
}
