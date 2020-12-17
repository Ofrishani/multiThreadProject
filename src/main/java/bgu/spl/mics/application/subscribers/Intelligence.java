package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private MissionInfo[] missions;

	public Intelligence(int id,MissionInfo[] mission) {
		super("intelligence"+id);
		this.missions = mission;
	}

	@Override
	protected void initialize() {
		//subscribe terminateBroad
		subscribeBroadcast(TerminateBroadcast.class, broad->{
			terminate();
		});

		//subscriber TickBroadcast
		subscribeBroadcast(TickBroadcast.class, broad->{
			for (int i=0; i<missions.length; i++){
				if(missions[i].getTimeIssued() == broad.getCurrTime()){
					//send MissionReceivedEvent
					getSimplePublisher().sendEvent(new MissionReceivedEvent(missions[i],broad.getCurrTime()));
				}
			}
		});
	}
}
