package Statemachine.rondes.SecondRoundPhases;

import Statemachine.rondes.SecondRound;

public abstract class SecondRoundBasePhase {

    protected SecondRound secondRound;
    public SecondRoundBasePhase(SecondRound secondRound){
        this.secondRound = secondRound;
    }
    public abstract void action();
    public abstract void nextPhase();
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
