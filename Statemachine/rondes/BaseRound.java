package Statemachine.rondes;

public abstract class BaseRound {

    protected Bussen bussen;


    public BaseRound(Bussen bussen){
        this.bussen = bussen;
    }

    public abstract void nextRound();
    public String subscribeTillMessageReceived(String topic){
        return this.bussen.subscribeTillMessageReceived(topic);
    }
    public void toggleButtons(boolean onOrOff){
        this.bussen.toggleButtons(onOrOff);
    }
    public boolean getRedOrBlack(int counter){
        return this.bussen.getRedOrBlack(counter);
    }

    public String toString(){
        return this.getClass().getSimpleName();
    }

    public void addToPoints(int pointsChange){
        this.bussen.addToPoints(pointsChange);
    }
    public int getPoints(){
        return this.bussen.getPoints();
    }
    public void spelResetCheck(){this.bussen.spelResetCheck();}
    public int lastReadCard(){return this.bussen.lastReadCard();}
    public void Say(String toSay){this.bussen.Say(toSay);}
    public void flashEyes(boolean GreenOrRed){this.bussen.flashEyes(GreenOrRed);}
    public void dispenseCard(){this.bussen.dispenseCard();}
    public void blueScanningEyes(){this.bussen.blueScanningEyes();}
    public void buttonAckSentence(){this.bussen.buttonAckSentence();}
    public void result(boolean rightOrWrong){

        int pointChange = 50;
        if(!rightOrWrong){
            pointChange = -50;
            System.out.println("");
        }
        this.bussen.flashEyes(rightOrWrong);
        this.bussen.addToPoints(pointChange);
        this.spreekPunten(rightOrWrong, pointChange);
        System.out.println(rightOrWrong + "     " + pointChange);
        if(rightOrWrong){
            this.bussen.dispenseCandy();
            this.bussen.youMayGrabCandy();
        }
    }

    public void spreekPunten(boolean rightOrWrong, int pointchange){
        String goodOrBad = "goed";
        String plusOrMinus = "plus";
        if (!rightOrWrong){
            goodOrBad = "fout";
            plusOrMinus = "min";
        }
        int pointchangeToSay = pointchange;
        if (pointchange < 0){
            pointchangeToSay *= -1;
        }
        this.Say("je hebt het " + goodOrBad + ", " + plusOrMinus + " " + pointchangeToSay + " punten");
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        this.Say("je hebt nu " + this.getPoints() + "punten");
    }

    //the getters and setters for the cards
    public void setPoints(int newPoints){
        this.bussen.setPoints(newPoints);
    }
    public int getCard1() {
        return bussen.getCard1();
    }

    public void setCard1(int card1) {
        this.bussen.setCard1(card1);
    }
    public int getCard2() {
        return bussen.getCard2();
    }

    public void setCard2(int card2) {
        this.bussen.setCard2(card2);
    }
    public int getCard3() {
        return bussen.getCard3();
    }

    public void setCard3(int card3) {
        this.bussen.setCard3(card3);
    }

    public void waitForPerson(){this.bussen.waitForPerson();}
}
