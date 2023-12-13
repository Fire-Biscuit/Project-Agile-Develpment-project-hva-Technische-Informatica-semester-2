package Statemachine.rondes.FirstRoundPhases;

import Statemachine.rondes.FirstRound;

public abstract class FirstRoundBasePhase {
    protected FirstRound firstRound;
    public FirstRoundBasePhase(FirstRound firstRound){
        this.firstRound = firstRound;
    }
    public abstract void action();
    public abstract void nextPhase();
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
