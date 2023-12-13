package Statemachine;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALBarcodeReader;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.Arrays;
import java.util.Objects;


public class ReadCard{

    //private static Application application;

    private String oldColour = "null";
    private String colourPlaceholder;
    private boolean timer = true;
    private Application application;
    private ALBarcodeReader barcode_service;
    private int lastReadCard;
    private boolean qrCodeDetected = false;
    private boolean detectionAllowed;
    private final int TIMERCOUNT = 2000; //1000 * 50miliseconds = 50 seconds

    //this constructor initializes the barcode service object and sets the application
    public ReadCard(Application application) throws Exception {
        try {
            this.application = application;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        this.barcode_service = new ALBarcodeReader(application.session());

        try {
            barcode_service.subscribe("test_barcode");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        this.detectionAllowed = false;
    }

    //this function checks to see if there's a qr-code in view and returns the containing int,
    // if the given time runs out it throws a TimeUpExeption
    public int barcodeEvent() throws TimeUpExeption{
        System.out.println("test___________________________________________________________________________________________");
        try {
            ALMemory alMemory = new ALMemory(this.application.session());
            alMemory.subscribeToEvent("BarcodeReader/BarcodeDetected", new EventCallback() {
                @Override
                public void onEvent(Object o) throws InterruptedException, CallError {
                    System.out.println(o.toString());
                    if (getDetectionAllowed()) {
                        barcodeDetection(o.toString());
                        setQrCodeDetected(true);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.detectionAllowed = true;
        this.setQrCodeDetected(false);

        int toReturn = 0;
        for (int i = 0; i < this.TIMERCOUNT; i++) {
            try {
                Thread.sleep(50);
                System.out.println("nu in wachten" + i);
                System.out.println(getLastReadCard());
                System.out.println(qrCodeDetected);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (this.qrCodeDetected) {
                System.out.println("detected");
                toReturn = getLastReadCard();
                break;
            }
            if(i == TIMERCOUNT - 1){
                throw new TimeUpExeption();
            }
        }
        return toReturn;
    }

    //the functions below are used in the callback because you cant directly access variables from there

    //this functions seperates the int from the string in which the qr-code is delivered
    private void barcodeDetection(String colourString){
        for (int i = 0; i < 10; i++) {
            if(colourString.contains(("number " + i))){
                setLastReadCard(i);
                System.out.println(getLastReadCard() + " CONFIRMED");
            }
        }
    }

    public boolean getDetectionAllowed() {return detectionAllowed;}
    public void setDetectionAllowed(boolean detectionAllowed) {this.detectionAllowed = detectionAllowed;}
    public void setQrCodeDetected(boolean input){this.qrCodeDetected = input;}

    public int getLastReadCard() {
        return lastReadCard;
    }

    public void setLastReadCard(int lastReadCard) {
        this.lastReadCard = lastReadCard;
    }
}
