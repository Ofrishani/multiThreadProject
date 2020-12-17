package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

/**
 * M send this event to Q in order to check gadget availability for specific mission.
 */
public class GadgetAvailableEvent implements Event<Object[]> {
    String gadget;

    /**
     * @param gad the gadget that needed for spicific mission.
     */
    public GadgetAvailableEvent(String gad){
        this.gadget = gad;
    }

    /**
     * @return the gadget requiered for the mission
     */
    public String getGadget() {return this.gadget;}

}
