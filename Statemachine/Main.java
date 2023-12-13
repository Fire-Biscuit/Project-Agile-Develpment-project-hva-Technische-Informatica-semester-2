package Statemachine;

import Statemachine.rondes.Bussen;

public class Main {
    public static void main(String[] args) {
        //because not all NAO robots have the same hostname this variable is made so that you can easily
        // efine the NAO's hostname before trying to connect to it
        String hostname = "Terminator";
        //bussen is a singleton object so that you can't make a 2nd nao robot object
        //because that will overwrite the nao and crash the code
        Bussen bussen = Bussen.getInstance(hostname);
    }
}
