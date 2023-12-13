package Statemachine.rondes;

import Statemachine.rondes.ThirdRoundPhases.ThirdRoundBasePhase;
import Statemachine.rondes.ThirdRoundPhases.ThirdRoundWaitingPhase;

public class ThirdRound extends BaseRound {
    private ThirdRoundBasePhase currentThirdRoundPhase;

    private boolean currentGuess;

    public ThirdRound(Bussen bussen) {
        super(bussen);
        this.currentThirdRoundPhase = new ThirdRoundWaitingPhase(this);
    }

    @Override
    public void nextRound() {
        System.out.println("__play again__");
        this.bussen.nextRound(new FirstRound(this.bussen));
    }

    public void youNeedToReturnCards() {
        this.bussen.youNeedToReturnCards();
    }

    public boolean checkpoints() {
        int card1 = this.bussen.getCard1();
        int card2 = this.bussen.getCard2();
        int card3 = this.bussen.getCard3();


        int lowestCard = card1;
        int highestCard = card2;

        if (card1 > card2) {
            lowestCard = card2;
            highestCard = card1;
        }

        System.out.println(lowestCard);
        System.out.println(highestCard);
        System.out.println(card3 + "\n");


        if (this.currentGuess) {
            if (card3 > lowestCard && card3 < highestCard) {
                System.out.println(1);
                return true;
            } else {
                System.out.println(2);
                return false;
            }
        } else {
            if (card3 < lowestCard || card3 > highestCard) {
                System.out.println(3);
                return true;
            } else {
                System.out.println(4);
                return false;
            }
        }
    }

    public void nextRoundPhase(ThirdRoundBasePhase nextPhase) {
        this.currentThirdRoundPhase = nextPhase;
    }

    public void setCurrentGuess(boolean currentGuess) {
        this.currentGuess = currentGuess;
    }
}
