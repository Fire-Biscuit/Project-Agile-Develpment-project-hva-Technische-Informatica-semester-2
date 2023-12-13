package Statemachine.rondes;

import Statemachine.rondes.FirstRoundPhases.FirstRoundBasePhase;
import Statemachine.rondes.FirstRoundPhases.FirstRoundSetupPhase;
import Statemachine.rondes.FirstRoundPhases.FirstRoundWaitingPhase;

public class FirstRound extends BaseRound {
    private FirstRoundBasePhase currentFirstRoundPhase;
    private boolean currentGuess;
    public FirstRound(Bussen bussen) {
        super(bussen);
        System.out.println("nu in de first round");
        this.currentFirstRoundPhase = new FirstRoundWaitingPhase(this);
    }


    public void nextRoundPhase(FirstRoundBasePhase nextPhase) {
        this.currentFirstRoundPhase = nextPhase;
    }

    @Override
    public void nextRound() {
        this.bussen.nextRound(new SecondRound(this.bussen));
    }

    /**
     * BELOW ARE GETTERS AND SETTERS
     */


    public boolean getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(boolean currentGuess) {
        this.currentGuess = currentGuess;
    }
}
