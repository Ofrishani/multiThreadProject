package bgu.spl.mics.application.passiveObjects;

import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

    private Map<String, Agent> agents;

    private static class SingeltonHolder {
        private static Squad single_instance = new Squad();
    }

    private Squad() {
        agents = new HashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        return SingeltonHolder.single_instance;
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        for (int i = 0; i < agents.length; i++) {
            String serialNumber = agents[i].getSerialNumber();
            this.agents.put(serialNumber, agents[i]);
        }
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
        for (String serial : serials) {
            agents.get(serial).release();
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time time ticks to sleep
     */
    public void sendAgents(List<String> serials, int time) throws InterruptedException {
        Thread.sleep(time * 100); //convert int to milliseconds.
        releaseAgents(serials);
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public boolean getAgents(List<String> serials) {
        if (isAnyoneMissing(serials)) return false;

        //sort serials list to prevent dead lock when 2 or more subscribers try to catch agents list.
        Collections.sort(serials);
        for (String subs : serials) {
            agents.get(subs).acquire();
        }
        return true;
    }
    // return if there is someone missin in required agent list.
    private boolean isAnyoneMissing(List<String> serials) {
        for (String serial : serials) {
            if (!agents.containsKey(serial)) {
                return true;
            }
        }
        return false;
    }

    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
        List<String> agentsNames = new LinkedList<>();
        if (!isAnyoneMissing(serials))
            for (String serial : serials) {
                agentsNames.add(agents.get(serial).getName());
            }
        return agentsNames;
    }

}
