package Statemachine.rondes.ThirdRoundPhases;

import Statemachine.rondes.ThirdRound;

public class ThirdRoundSetupPhase extends ThirdRoundBasePhase{
    public ThirdRoundSetupPhase(ThirdRound thirdRound) {
        super(thirdRound);
        System.out.println("\nnow in the 3rd round");
        action();
    }

    @Override
    public void action() {
        System.out.println("he grabs, reads than puts down the 3rd");
        this.thirdRound.dispenseCard();
        //this line is temporary
        this.thirdRound.setCard3(this.thirdRound.lastReadCard());
        System.out.println("CARD 3 = " + this.thirdRound.getCard3());
        nextPhase();
    }

    @Override
    public void nextPhase() {
        this.thirdRound.nextRoundPhase(new ThirdRoundActionPhase(this.thirdRound));
    }
}
