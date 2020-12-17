package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {
    private String serialNumber;
    private String name;
    private Boolean available = true;

    /**
     * Sets the serial number of an agent.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Retrieves the serial number of an agent.
     * <p>
     *
     * @return The serial number of an agent.
     */
    public String getSerialNumber() {
        return this.serialNumber;
    }

    /**
     * Sets the name of the agent.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the agent.
     * <p>
     *
     * @return the name of the agent.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves if the agent is available.
     * <p>
     *
     * @return if the agent is available.
     */
    public boolean isAvailable() {
        return this.available;
    }

    /**
     * Acquires an agent.
     */
    public synchronized void acquire() {
        try {
            while (!(available)) {
                wait();
            }
        } catch (InterruptedException ex) {
        }
        available = false;
    }

    /**
     * Releases an agent.
     */
    public synchronized void release() {
        available = true;
        notifyAll();
    }
}
