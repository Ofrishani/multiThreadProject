package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private int serialNumber;
    private Squad squad;
    private AtomicInteger countoddMoneyPenny = new AtomicInteger();

    public Moneypenny(int SerialNumber) {
        super("Moneypenny"+SerialNumber);
        this.serialNumber = SerialNumber;
        this.squad = Squad.getInstance();
    }

    @Override
    protected void initialize() {
        //subscriber TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, broad -> {
            terminate();
        });

        //AgentsAvailableEvent
        subscribeEvent(AgentsAvailableEvent.class, message -> {
            boolean ans = squad.getAgents(message.getAgents());
            List<String> serialTemp = message.getAgents();
            Object[] result = {ans, serialNumber,squad.getAgentsNames(message.getAgents())};
            complete(message, result);
            if(ans) {
                Future<Integer> getMp_whatToDo = message.getMp_whatToDo();
                int getMp_whatToDo_int=getMp_whatToDo.get();
                if(getMp_whatToDo_int==-1)
                    squad.releaseAgents(serialTemp);
                else
                    squad.sendAgents(serialTemp,getMp_whatToDo_int);
            }
        });
    }

}
