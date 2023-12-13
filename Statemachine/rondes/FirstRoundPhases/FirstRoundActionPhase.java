package Statemachine.rondes.FirstRoundPhases;

import Statemachine.rondes.FirstRound;

public class FirstRoundActionPhase extends FirstRoundBasePhase{
    public FirstRoundActionPhase(FirstRound firstRound) {
        super(firstRound);
        System.out.println(toString());
        action();
    }

    @Override
    public void action() {
        System.out.println("hij neemt je button input en kijkt of je gelijk hebt");
        System.out.println("arrayguess");
        System.out.println(this.firstRound.getRedOrBlack(this.firstRound.getCard1()));
        System.out.println("currentguess");
        System.out.println(this.firstRound.getCurrentGuess());
        System.out.println("___________");
        if (this.firstRound.getRedOrBlack(this.firstRound.getCard1()) == this.firstRound.getCurrentGuess()){
            this.firstRound.result(true);
        } else {
            this.firstRound.result(false);
        }
        this.firstRound.spelResetCheck();
        nextPhase();
    }

    @Override
    public void nextPhase() {
        System.out.println("next Round");
        this.firstRound.nextRound();
    }
}
