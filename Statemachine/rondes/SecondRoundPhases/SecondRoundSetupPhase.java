package Statemachine.rondes.SecondRoundPhases;

import Statemachine.rondes.SecondRound;

public class SecondRoundSetupPhase extends SecondRoundBasePhase{
    public SecondRoundSetupPhase(SecondRound secondRound) {
        super(secondRound);
        System.out.println(toString());
        action();
    }

    @Override
    public void action() {
        System.out.println("he now grabs and reads the second card and puts it down");
        System.out.println("is this card HIGHER than the previous");
        this.secondRound.dispenseCard();
        this.secondRound.setCard2(this.secondRound.lastReadCard());
        //this line is temporary
        System.out.println("CARD 2 = " + this.secondRound.getCard2());
        nextPhase();
    }

    @Override
    public void nextPhase() {
        this.secondRound.nextRoundPhase(new SecondRoundActionPhase(this.secondRound));
    }
}
