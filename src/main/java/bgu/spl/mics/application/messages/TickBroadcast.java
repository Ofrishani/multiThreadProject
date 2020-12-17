package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
/**
 * This broadcast announced all subscriber what is the current time tick.
 */
public class TickBroadcast implements Broadcast {
    int currentTime;

    /**
     * @param curr current time tick.
     */
    public TickBroadcast (int curr){
        this.currentTime = curr;

    }
    /**
     * @return current time tick.
     */
    public int getCurrTime(){return currentTime;}
}
