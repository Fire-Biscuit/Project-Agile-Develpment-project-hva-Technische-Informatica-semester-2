package Statemachine.rondes.FirstRoundPhases;

import Statemachine.rondes.FirstRound;

public class FirstRoundSetupPhase extends FirstRoundBasePhase{
    public FirstRoundSetupPhase(FirstRound firstRound) {
        super(firstRound);
        System.out.println("hij zit nu in de setup fase van ronde 1");
        action();
    }

    @Override
    public void action() {
        System.out.println("leest en legt nu kaart 1 neer");
        this.firstRound.dispenseCard();
        this.firstRound.setCard1(this.firstRound.lastReadCard());
        System.out.println("CARD 1 = " + this.firstRound.getCard1());
        //deze regel is dus tijdelijk
        nextPhase();
    }

    @Override
    public void nextPhase() {
        this.firstRound.nextRoundPhase(new FirstRoundActionPhase(this.firstRound));
    }
}
