package Statemachine;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NAO {
    private ApplicationCache applicationCache;
    private Application application;
    private ReadCard readCard;
    private final float VOLUME = 0.2F;
    private final String NAOLANGUAGE = "Dutch";
    private ALFaceDetection faceDetection;
    private boolean faceDetected;
    private boolean faceDetectionAllowed;
    private final String[] CANDYSENTENCES = {
            "hier is een snoepje",
            "gefeliciteerd",
            "eet smakelijk",
            "dit heb je verdiend"
    };
    private ALAudioPlayer alAudioPlayer;
    private ALTextToSpeech alTextToSpeech;
    private final String[] BUTTONACKSENTENCES = {
            "eens zien of je gelijk hebt",
            "laat me nu een kaart zien",
            "houd nu een kaart voor mijn gezicht",
            "pak nu een kaart"
    };
    private final List<String> JOY = Arrays.asList(
            "animations/Stand/Gestures/Yes_1",
            "animations/Stand/Gestures/Yes_2",
            "animations/Stand/Gestures/Yes_3",
            "animations/Stand/Gestures/Enthusiastic_4",
            "animations/Stand/Gestures/Enthusiastic_5"
    );
    private final List<String> SAD = Arrays.asList(
            "animations/Stand/Gestures/No_1",
            "animations/Stand/Gestures/No_2",
            "animations/Stand/Gestures/No_3",
            "animations/Stand/Gestures/No_8",
            "animations/Stand/Gestures/No_9"
    );

    private final int MQTTPort = 9559;

    public NAO(String hostname){

        try {
            this.alAudioPlayer = new ALAudioPlayer(this.getApplication().session());
            this.faceDetection = new ALFaceDetection(this.application.session());
            this.alTextToSpeech = new ALTextToSpeech(this.application.session());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.applicationCache = new ApplicationCache(hostname + ".local", this.MQTTPort);
        this.application = applicationCache.getApplication();
        try {
            this.readCard = new ReadCard(this.getApplication());
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("no readcard made !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        this.setVolume(this.VOLUME);
        this.speakDutch();
        this.setPosture();
    }
    private final String[] CARDRETURNSENTENCES = {
            "`leg nu de kaarten terug`",
            "ruim nu de kaarten op",
            "doe de kaarten in het bakje"
    };
    public void youNeedToReturncards(){
        int sentence = (int)(Math.random() * this.CARDRETURNSENTENCES.length);
        this.Say(CARDRETURNSENTENCES[sentence]);
    }

    public void youMayGrabCandy(){

        int sentence = (int)(Math.random() * this.BUTTONACKSENTENCES.length);
        this.Say(CANDYSENTENCES[sentence]);
    }
    //this randomly speaks one of the sentences defined above with BUTTONACKSENTENCES
    public void buttonAckSentense(){
        this.blueScanningEyes();
        int sentence = (int)(Math.random() * this.BUTTONACKSENTENCES.length);
        this.Say(this.BUTTONACKSENTENCES[sentence]);
    }
    //this function accesses the read barcode event, and should a TimeUpExeption
    //be caught it will throw it to the bussen class from where it's accessed
    public int barcodeEvent() throws TimeUpExeption{
        return this.readCard.barcodeEvent();
    }

    public Application getApplication() {
        return application;
    }
    public void setApplication(Application app) {
        this.application = app;
    }

    public String toString() {
        return "Nao = is: " + this.getClass().getSimpleName();
    }


    //this function waits till a face is detected and than jumps back to the code
    public void waitForPerson(){
        System.out.println("now waiting for person");
        System.out.println("_________________________________________");
        try {
            this.faceDetection = new ALFaceDetection(this.application.session());
            this.faceDetection.subscribe("faceDetector");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        try {
            ALMemory alMemory = new ALMemory(this.application.session());
            alMemory.subscribeToEvent("FaceDetected", new EventCallback() {
                @Override
                public void onEvent(Object o) throws InterruptedException, CallError {
                    if(getFaceDetectionAllowed()){
                        System.out.println(o.toString());
                        System.out.println("face detected!!!!!");
                        setFaceDetected(true);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.faceDetectionAllowed = true;
        this.faceDetected = false;
        while (true){
            try{
                Thread.sleep(500);
                System.out.println("waiting for face");
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            if(faceDetected){
                break;
            }
        }

        this.faceDetectionAllowed = true;
        this.faceDetected = false;

        while (true){
            try{
                Thread.sleep(100);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            if(faceDetected){
                this.faceDetectionAllowed = false;
                break;
            }
        }
    }

    //this sets the nao's language to dutch at the beginning of the code
    public void speakDutch(){
        try {
            this.alTextToSpeech = new ALTextToSpeech(this.application.session());
            this.alTextToSpeech.setLanguage(this.NAOLANGUAGE);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("language set went wrong");
        }
    }

    //this sets the nao's volume at the beginning of the code
    public void setVolume(float volume){
        try{
            this.alAudioPlayer.setMasterVolume(0.7F);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        try {
            this.alTextToSpeech.setVolume(volume);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //this turn the nao's eye blue
    public void blueScanningEyes(){
        try{
            ALLeds eyeProxy = new ALLeds(this.application.session());
            eyeProxy.fadeRGB("FaceLeds", "blue", 1f);
        } catch (Exception e){
            System.out.println(e.getMessage() + "eyes not turning blue");
        }
    }

    // From the result if the user was right or wrong, the NAO robot has a reaction. This can both be positive and negative.
    public void robotReaction(boolean GreenOrRed) {
        int temporaryEyeColour;
        String robotReaction;

        // When the user chose the right answer the eyes will be green and an animation will be assigned.
        if (GreenOrRed){
            temporaryEyeColour = 0x00009900; //green
            robotReaction = randomReaction(true);
        }
        // However when the user is wrong the eyes will be red and a negative animation will be assigned.
        else {
            temporaryEyeColour= 0x00990000; //red       //brg
            robotReaction = randomReaction(false);
        }
        // Objects are made here from the NAQI api
        ALLeds eyeProxy;
        ALAnimationPlayer animationPlayer;

        // The above parameters are being used to display the reaction with the ALLeds and ALAnimationPlayer API.
        try {
            eyeProxy = new ALLeds(this.application.session());
            animationPlayer = new ALAnimationPlayer(this.application.session());
            eyeProxy.rotateEyes(temporaryEyeColour,2.0f,2.0f);
            animationPlayer.run(robotReaction);
        } catch (Exception e) {
            System.out.println("eye colour or animation not working");
        }
    }

    // A randomizer chooses from the final string above in the file to send back to robotReaction.
    private String randomReaction(boolean JoyOrSad) {
        String animation;
        Random randomJOYORSAD = new Random();
        if (JoyOrSad) {
            animation = JOY.get(randomJOYORSAD.nextInt(JOY.size()));
        } else {
            animation = SAD.get(randomJOYORSAD.nextInt(SAD.size()));
        }
        return animation;
    }

    // To make animations work the NAO robot has to be in the 'Stand' state.
    public void setPosture() {
        ALRobotPosture robotPosture = null;
        try {
            robotPosture = new ALRobotPosture(this.application.session());
            robotPosture.goToPosture("Stand", 0.2F);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //this makes the nao say a given string of characters
    public void Say(String toSay){
        try {
            // Create an ALTextToSpeech object and link it to your current session
            ALTextToSpeech tts = new ALTextToSpeech(this.application.session());
            tts.resetSpeed();
            // Make your robot say something
            tts.say(toSay);
            System.out.println("hij zegt: " + toSay);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //these 2 functions are used in the callback because it can't directly access variables
    public void setFaceDetected(boolean faceDetected) {
        this.faceDetected = faceDetected;
    }
    public boolean getFaceDetectionAllowed() {
        return faceDetectionAllowed;
    }
}
