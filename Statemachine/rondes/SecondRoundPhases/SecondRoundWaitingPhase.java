package Statemachine.rondes.SecondRoundPhases;

import Statemachine.rondes.SecondRound;

public class SecondRoundWaitingPhase extends SecondRoundBasePhase{
    public SecondRoundWaitingPhase(SecondRound secondRound) {
        super(secondRound);
        System.out.println(toString());
        action();
    }

    @Override
    public void action() {
        this.secondRound.Say("Ronde 2");
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        this.secondRound.Say("wordt deze kaart hoger dan de vorige kaart");
        System.out.println("waiting for button input");
        this.secondRound.toggleButtons(true);
        System.out.println("tells you to press a button");
        System.out.println("wacht op antwoord van de speler via de button en stopt met wachten als hij wordt ingedrukt");
        String mqttPayload = (this.secondRound.subscribeTillMessageReceived("schardm/higherOrLower"));
        System.out.printf("THE MQTT PAYLOAD = %s\n", mqttPayload);
        switch (mqttPayload){
            case "0":
                System.out.println("BUTTON INPUT = 0");
                this.secondRound.setCurrentGuess(false);
                break;
            case "1":
                System.out.println("BUTTON INPUT = 1");
                this.secondRound.setCurrentGuess(true);
                break;
        }
        this.secondRound.toggleButtons(false);
        this.secondRound.buttonAckSentence();
        nextPhase();
    }

    @Override
    public void nextPhase() {
        this.secondRound.nextRoundPhase(new SecondRoundSetupPhase(this.secondRound));
    }
}
