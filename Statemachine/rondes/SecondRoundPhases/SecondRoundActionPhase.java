package Statemachine.rondes.SecondRoundPhases;

import Statemachine.rondes.SecondRound;

public class SecondRoundActionPhase extends SecondRoundBasePhase{

    public SecondRoundActionPhase(SecondRound secondRound) {
        super(secondRound);
        System.out.println("now action phase of round 2");
        action();
    }

    @Override
    public void action() {
        System.out.println("takes button input and checks if it's higher than the first card");
        if (secondRound.getCurrentGuess()){
            if(this.secondRound.getCard2() > this.secondRound.getCard1()){
                this.secondRound.result(true);
            } else {
                this.secondRound.result(false);
            }
        } else if (!secondRound.getCurrentGuess()) {
            if(this.secondRound.getCard2() < this.secondRound.getCard1()){
                this.secondRound.result(true);
            } else {
                this.secondRound.result(false);
            }
        }
        System.out.printf("CURRENT POINTS:          %d\n", this.secondRound.getPoints());
        this.secondRound.spelResetCheck();
        nextPhase();
    }

    @Override
    public void nextPhase() {
        this.secondRound.nextRound();
    }
}
