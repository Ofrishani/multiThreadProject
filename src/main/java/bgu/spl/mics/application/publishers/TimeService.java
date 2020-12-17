package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private int duration;
    private int countingClockTick;
    private final int waitTime = 100;

    public TimeService(int duration) {
        super("Time Service");
        this.duration = duration;
        countingClockTick = 1;
    }

    @Override
    protected void initialize() {
    }

    @Override
    public void run() {
        //if we are not in the final tick, send TickBroadcast
        while (countingClockTick < duration) {
            getSimplePublisher().sendBroadcast(new TickBroadcast(countingClockTick));

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
            }
            countingClockTick++;
        }
        //Last time tick - send Terminate.
        if (countingClockTick == duration) {
            getSimplePublisher().sendBroadcast(new TerminateBroadcast());
        }
    }

}
