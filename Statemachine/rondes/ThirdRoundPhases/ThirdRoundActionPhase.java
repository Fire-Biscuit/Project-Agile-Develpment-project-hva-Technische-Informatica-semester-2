package Statemachine.rondes.ThirdRoundPhases;

import Statemachine.rondes.ThirdRound;

public class ThirdRoundActionPhase extends ThirdRoundBasePhase{
    public ThirdRoundActionPhase(ThirdRound thirdRound) {
        super(thirdRound);
        action();
    }

    @Override
    public void action() {
        System.out.println("checks if card is between cards 1 and 2");
        this.thirdRound.result(this.thirdRound.checkpoints());

        System.out.printf("CURRENT POINTS:          %d\n", this.thirdRound.getPoints());
        this.thirdRound.spelResetCheck();
        this.thirdRound.youNeedToReturnCards();
        nextPhase();
    }

    @Override
    public void nextPhase() {
        this.thirdRound.nextRound();
    }
}
