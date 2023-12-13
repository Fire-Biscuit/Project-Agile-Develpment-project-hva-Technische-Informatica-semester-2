package Statemachine.rondes.ThirdRoundPhases;

import Statemachine.rondes.ThirdRound;

public class ThirdRoundWaitingPhase extends ThirdRoundBasePhase{

    public ThirdRoundWaitingPhase(ThirdRound thirdRound) {
        super(thirdRound);
        action();
    }

    @Override
    public void action() {
        this.thirdRound.Say("Ronde 3");
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        this.thirdRound.Say("is deze kaart tussen de vorige kaarten");
        System.out.println("waiting for button input");
        this.thirdRound.toggleButtons(true);
        System.out.println("is de kaart tussen de vorige kaarten?");
        System.out.println("wacht op antwoord van de speler via de button en stopt met wachten als hij wordt ingedrukt");
        String mqttPayload = (this.thirdRound.subscribeTillMessageReceived("schardm/higherOrLower"));
        System.out.printf("THE MQTT PAYLOAD = %s\n", mqttPayload);
        switch (mqttPayload){
            case "0":
                System.out.println("BUTTON INPUT = 0");
                this.thirdRound.setCurrentGuess(false);
                break;
            case "1":
                System.out.println("BUTTON INPUT = 1");
                this.thirdRound.setCurrentGuess(true);
                break;
        }
        this.thirdRound.toggleButtons(false);
        this.thirdRound.buttonAckSentence();
        nextPhase();
    }

    @Override
    public void nextPhase() {
        this.thirdRound.nextRoundPhase(new ThirdRoundSetupPhase(this.thirdRound));
    }
}
