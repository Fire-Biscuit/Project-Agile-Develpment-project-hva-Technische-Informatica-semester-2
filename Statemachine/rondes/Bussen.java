package Statemachine.rondes;

import Statemachine.Mqtt;
import Statemachine.NAO;
import Statemachine.TimeUpExeption;

public class Bussen {
    private static Bussen instance;
    private int points;
    private int card1;
    private int card2;
    private int card3;
    private boolean[] redOrBlack = new boolean[]{true, false, true, false, true, false, true, false, true, false};
    /*here are the colours of the cards saved //  true = red, false = black(white)
    0 = red, 1 = black, 2 = red, 3 = black, 4 = red, 5 = black, 6 = red, 7  = black, 8 = red, 9  = black
     */
    private int STARTINGPOINTS = 100;
    private BaseRound currentRound;
    private Mqtt mqtt;
    private NAO nao;

    //this constructor initializes the nao and mqtt objects and starts a new game
    public Bussen(String hostname) {
        this.nao = new NAO(hostname);
        this.mqtt = new Mqtt();
        this.newGame();
    }

    //this returns the current instance and stets it to null so that a 2nd object can't be made, thus making it singleton
    public static Bussen getInstance(String hostname) {
        if (Bussen.instance == null) {
            Bussen.instance = new Bussen(hostname);
        }
        return Bussen.instance;
    }

    //this function is used whenever the game needs to be reset, it resets the points, game and lets the player know
    private void newGame() {
        this.setPoints(0);
        this.mqtt.toggleButtons(false);
        this.waitForPerson();
        System.out.println("-----------NEW GAME----------------");
        this.setPoints(100);
        this.nao.Say("welkom bij bussen, je hebt nu 100 punten");
        currentRound = new FirstRound(this);
    }

    //this function is used to access the mqtt function which subscribes to a topic untill it receives a message and than ubsubscribes,
    //should the gives time run out the function will return a TimeUpException which will be caught and resets the game
    public String subscribeTillMessageReceived(String topic) {
        String output = "0";
        try {
            output = mqtt.subscribeTillMessageReceived(topic);
        } catch (TimeUpExeption e){
            this.newGame();
        }
        return output;
    }
    public void youMayGrabCandy(){
        this.nao.youMayGrabCandy();
    }
    public void youNeedToReturnCards(){
        this.nao.youNeedToReturncards();
    }

    //this method is used to add or remove points from the current points
    public void addToPoints(int pointsChange) {
        int pointsChanged = pointsChange;
        if(this.points >= 200){
            if(pointsChange > 0) {
                pointsChanged /= 2;
            } else if (pointsChange < 0){
                pointsChanged *= 2;
            }
        }
        this.setPoints(this.points += pointsChanged);
    }

    //this method resets the current points to 0, this method is used during the beginning of each game
    public void resetPoints() {
        this.setPoints(STARTINGPOINTS);
    }

    //this is the setter for the points variable, but it also calls the mqtt function to
    // update the points on the 4 digit 7-segment display
    public void setPoints(int newPoints) {
        this.points = newPoints;
        this.mqtt.updatePoints(this.points);
    }

    //this checks if you are out of points, if you are than it tells the player and resets the bussen game
    public void spelResetCheck() {
        if (points <= 0) {
            resetPoints();
            for (int i = 0; i < 200; i++) {
                System.out.print("!!!!!");
            }
            System.out.println(" \n");
            this.nao.Say("helaas, je het spel is over");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            this.nao.Say("we beginnen nu opnieuw");
            this.nao.Say("welkom bij bussen, je hebt nu 100 punten");

            this.currentRound = new FirstRound(this);   //dit reset het spel
        }
    }

    //this lets the nao check for a qr-code and returns it if one is registerd,
    //if a TimeUpExeption is caught than it resets the game
    public int lastReadCard() {
        int toReturn = 0;
        try {
            toReturn = this.nao.barcodeEvent();
        } catch (TimeUpExeption e) {
            this.newGame();
        }
        return toReturn;
    }

    //the functions below are used to call functions from other objects
    public void Say(String toSay) {
        this.nao.Say(toSay);
    }

    public void toggleButtons(boolean onOrOff) {
        mqtt.toggleButtons(onOrOff);
    }

    public void flashEyes(boolean GreenOrRed) {
        this.nao.robotReaction(GreenOrRed);
    }

    public void waitForPerson() {
        this.nao.waitForPerson();
    }

    public void dispenseCard() {
        this.mqtt.dispenseCard();
    }

    public void blueScanningEyes() {
        this.nao.blueScanningEyes();
    }

    public void dispenseCandy() {
        this.mqtt.dispenseCandy();
    }

    public void buttonAckSentence() {
        this.nao.buttonAckSentense();
    }


    //all methods below are either getters or setters
    public int getPoints() {
        return this.points;
    }

    public boolean getRedOrBlack(int counter) {
        return redOrBlack[counter];
    }

    public void nextRound(BaseRound nextRound) {
        this.currentRound = nextRound;
    }

    public int getCard1() {
        return card1;
    }

    public void setCard1(int card1) {
        this.card1 = card1;
    }

    public int getCard2() {
        return card2;
    }

    public void setCard2(int card2) {
        this.card2 = card2;
    }

    public int getCard3() {
        return card3;
    }

    public void setCard3(int card3) {
        this.card3 = card3;
    }
}
