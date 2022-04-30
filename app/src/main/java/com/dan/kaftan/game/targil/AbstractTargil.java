package com.dan.kaftan.game.targil;

abstract public class AbstractTargil implements Targil {

    protected int firstnum;
    protected int secondNum;
    protected String operator;

    public AbstractTargil() {
        super();
    }

    // CTR
    public AbstractTargil(int firstnum, int secondNum, String operator) {
        this.firstnum = firstnum;
        this.secondNum = secondNum;
        this.operator = operator;
    }

    @Override
    public void setFirstNum(int firstNum) {
        this.firstnum = firstNum;
    }

    @Override
    public int getFirstNum() {
        return firstnum;
    }

    @Override
    public void setSecondNum(int SecondNum) {
        this.secondNum = secondNum;
    }

    @Override
    public int getSecondNum() {
        return secondNum;
    }

    @Override
    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public String getTargilAsString(){
        return firstnum + " " + operator + " " + secondNum;
    }

    @Override
    public abstract int calc();
}
