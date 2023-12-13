package Statemachine.rondes.ThirdRoundPhases;

import Statemachine.rondes.SecondRound;
import Statemachine.rondes.ThirdRound;

public abstract class ThirdRoundBasePhase {
    protected ThirdRound thirdRound;
    public ThirdRoundBasePhase(ThirdRound thirdRound){
        this.thirdRound = thirdRound;
    }
    public abstract void action();
    public abstract void nextPhase();
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
