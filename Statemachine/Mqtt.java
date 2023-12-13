package Statemachine;

import org.eclipse.paho.client.mqttv3.*;

public class Mqtt {
    //these are the mqtt cridentials used for the mqtt server
    private final String MQTT_HOST = "tcp://mqtt.hva-robots.nl:1883";
    private final String MQTT_CLIENT_ID = "schardm_%";
    private final String MQTT_USERNAME = "schardm";
    private final String MQTT_PASSWORD = "4Oyhf3DAoSzxYc9QciX8";
    private boolean messageReceived;

    private MqttClient client;
    private String thisMqttPayload;

    private final int TIMERCOUNT = 1000; //1000 * 50miliseconds = 50 seconds

    //this constructor sets up the mqtt object,
    //sets the callback
    //and subscribes to the right topics
    public Mqtt() {
        try {
            client = new MqttClient(MQTT_HOST, MQTT_CLIENT_ID);
        } catch (MqttException e) {
            System.out.println("kan geen client maken");
            throw new RuntimeException(e);
        }

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(MQTT_USERNAME);
        connectOptions.setPassword(MQTT_PASSWORD.toCharArray());

        try {
            client.connect(connectOptions);
        } catch (MqttException e) {
            System.out.println("kan geen verbinding maken met de mqtt server");
            throw new RuntimeException(e);
        }
        System.out.print("verbonden? ");
        System.out.println(client.isConnected());

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println("message received!");
                System.out.println(mqttMessage.toString());
                setMqttPayload(mqttMessage.toString());
                setMessageReceived(true);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        this.toggleButtons(false);
    }
    private void setMessageReceived(boolean messageReceived){
        this.messageReceived = messageReceived;
    }

    public void setMqttPayload(String payload){
        this.thisMqttPayload = payload;
    }

    //this function subscribest to a topic given as an argument and unsubscribes and returns once it receives a message,
    //if no message is received after a certain time than it throws a TimeUpExeption
    public String subscribeTillMessageReceived(String topic) throws TimeUpExeption{
        this.messageReceived = false;

        subscribe(topic);

        for (int i = 0; i < TIMERCOUNT; i++) {

            try{
            Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(i);

            if(messageReceived){
                break;
            }
            if(i == TIMERCOUNT - 1){
                throw new TimeUpExeption();
            }
        }
        unsubscribe(topic);
        return this.thisMqttPayload;
    }

    //this subscribes to a given topic
    private void subscribe(String topic){
        try {
            client.subscribeWithResponse(topic);
            System.out.printf("subscribed to %s\n", topic);
        } catch (MqttException e) {
            System.out.println("EXEPTION CAUGHT: kan niet subscriben with response");
        }
    }
    //this unsubscribes from a given topic
    private void unsubscribe(String topic){
        try {
            client.unsubscribe(topic);
            System.out.printf("unsubscribed to %s\n", topic);
        } catch (MqttException e) {
            System.out.println("EXEPTION CAUGHT: kan niet unsubscriben");
        }
    }

    //this sends a message to either turn the buttons on or off
    public void toggleButtons(boolean onOrOff){
        MqttMessage message = new MqttMessage();
        String s = "0";
        if (onOrOff){
            s = "1";
        }
        byte[] byteArray = s.getBytes();
        message.setPayload(byteArray);
        try {
            client.publish("schardm/activateButtons", message);
            System.out.printf("buttons toggled %b\n", onOrOff);
        } catch (MqttException e) {
            System.out.println("exeption caught kan geen schardm/activateButtons publishen");
        }
    }

    //this sends a message to dispense a card from the card dispenser
    public void dispenseCard() {
        MqttMessage message = new MqttMessage();
        byte[] payload = "0".getBytes();
        message.setPayload(payload);
        try {
            client.publish("schardm/dispenseCard", message);
            System.out.println("_________________card dispense_____________________________________");
        } catch (MqttException e) {
            System.out.println("exeption caught kan geen schardm/aan publishen");
        }
    }

    //this sends a message to update the points on the 7 segment display
    public void updatePoints(int points) {
        System.out.println("POINT UPDATE!!!!!!!!!");

        MqttMessage message = new MqttMessage();
        int payloadPoints = 10000 + points;
        if(payloadPoints < 10000){
            payloadPoints = 10000;
        }
        byte[] payload = String.valueOf(payloadPoints).getBytes();

        message.setPayload(payload);
        System.out.println(payload.toString());
        try {
            client.publish("schardm/pointUpdate", message);
            System.out.println("_________________point update_____________________________________");
        } catch (MqttException e) {
            System.out.println("exeption caught kan geen schardm/pointUpdate publishen");
        }
    }

    //this sends a message to make the candy dispenser dispense a candy
    public void dispenseCandy() {
        MqttMessage message = new MqttMessage();
        byte[] payload = "0".getBytes();
        message.setPayload(payload);
        try {
            client.publish("schardm/dispenseCandy", message);
            System.out.println("_________________candy dispense_____________________________________");
        } catch (MqttException e) {
            System.out.println("exeption caught kan geen schardm/dispenseCandy publishen");
        }
    }
}
