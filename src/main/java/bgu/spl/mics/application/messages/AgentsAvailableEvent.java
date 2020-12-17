package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;

/**
 * M send this event to MoneyPenney in order to check agents availability for specific mission.
 */

public class AgentsAvailableEvent implements Event<Object[]> {
    List<String> agents;
    Future<Integer> mp_whatToDo;

    /**
     * @param agentsInput the agents that needed for spicific mission.
     */
    public AgentsAvailableEvent(List<String> agentsInput) {
        this.agents = agentsInput;
        mp_whatToDo = new Future<>();
    }

    /**
     * @return future that tell MP what to do.
     */
    public Future<Integer> getMp_whatToDo() {
        return mp_whatToDo;
    }

    /**
     * @return the agents requiered for the mission
     */
    public List<String> getAgents() {
        return agents;
    }
}

