package Statemachine.rondes.FirstRoundPhases;

import Statemachine.rondes.FirstRound;

public class FirstRoundWaitingPhase extends FirstRoundBasePhase{
    public FirstRoundWaitingPhase(FirstRound firstRound) {
        super(firstRound);
        System.out.println(toString());
        action();
    }

    @Override
    public void action() {
        this.firstRound.Say("Ronde 1");
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        this.firstRound.toggleButtons(true);
        this.firstRound.Say("wordt deze kaart rood");
        System.out.println("vraagt wat je denkt dat het is en wacht op antwoord van " +
                "de speler via de button en stopt met wachten als hij wordt ingedrukt");
        String mqttPayload = (this.firstRound.subscribeTillMessageReceived("schardm/higherOrLower"));
        System.out.printf("THE MQTT PAYLOAD = %s\n", mqttPayload);
        switch (mqttPayload){
            case "0":
                System.out.println("BUTTON INPUT = 0");
                this.firstRound.setCurrentGuess(false);
                break;
            case "1":
                System.out.println("BUTTON INPUT = 1");
                this.firstRound.setCurrentGuess(true);
                break;
        }
        this.firstRound.toggleButtons(false);
        this.firstRound.buttonAckSentence();
        nextPhase();
    }

    @Override
    public void nextPhase() {
        this.firstRound.nextRoundPhase(new FirstRoundSetupPhase(this.firstRound));
    }
}
