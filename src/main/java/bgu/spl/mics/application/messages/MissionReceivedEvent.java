package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
/**
 *Intelligence send this event to M.
 * M responsible to execute the mission according to mission info.
 */
public class MissionReceivedEvent implements Event<Void> {
    MissionInfo mission;
    int TimeIssued;

    /**
     * @param m the mission that needs to be execute.
     * @param time mission time issued.
     */
    public MissionReceivedEvent(MissionInfo m, int time){
        this.mission=m;
        this.TimeIssued= time;
    }
    /**
     * @return the mission for execute.
     */
    public MissionInfo getMission(){ return mission;}
}
