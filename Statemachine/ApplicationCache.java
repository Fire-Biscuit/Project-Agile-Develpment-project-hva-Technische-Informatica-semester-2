package Statemachine;

import com.aldebaran.qi.Application;

public class ApplicationCache {

    public Application application;
    private String naam;

    public ApplicationCache(String hostname, int port) {
        String robotUrl = "tcp://" + hostname + ":" + port;
        // Create a new application
        this.application = new Application(new String[]{}, robotUrl);
        // Start your application
        application.start();
    }

    public Application getApplication(){
        return this.application;
    }
}
