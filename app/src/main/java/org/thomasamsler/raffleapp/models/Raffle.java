package org.thomasamsler.raffleapp.models;

/**
 * Created by tamsler on 11/20/14.
 */
public class Raffle {

    private String id;
    private String name;
    private String pin;

    public Raffle(String id, String name, String pin) {

        this.id = id;
        this.name = name;
        this.pin = pin;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getPin() {

        return pin;
    }

    public void setPin(String pin) {

        this.pin = pin;
    }
}
