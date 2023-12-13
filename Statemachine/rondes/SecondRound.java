package Statemachine.rondes;

import Statemachine.NAO;
import Statemachine.rondes.SecondRoundPhases.SecondRoundBasePhase;
import Statemachine.rondes.SecondRoundPhases.SecondRoundSetupPhase;
import Statemachine.rondes.SecondRoundPhases.SecondRoundWaitingPhase;

public class SecondRound extends BaseRound {
    private SecondRoundBasePhase secondRoundBasePhase;

    private boolean currentGuess;

    public SecondRound(Bussen bussen) {
        super(bussen);
        System.out.println("\nnow in the second round");
        this.secondRoundBasePhase = new SecondRoundWaitingPhase(this);
    }

    @Override
    public void nextRound() {
        this.bussen.nextRound(new ThirdRound(this.bussen));
    }

    public void nextRoundPhase(SecondRoundBasePhase nextPhase) {
        this.secondRoundBasePhase = nextPhase;
    }


    //the methods below are setters and getters

    public int getCard2() {
        return bussen.getCard2();
    }

    public void setCard2(int card2) {
        this.bussen.setCard2(card2);
    }

    public boolean getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(boolean currentGuess) {
        this.currentGuess = currentGuess;
    }
}
