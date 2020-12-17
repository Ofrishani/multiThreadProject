package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private Inventory inventory;
	private int Timetick;

	public Q() {
		super("Q");
		this.inventory = Inventory.getInstance();
		this.Timetick = 0;
	}

	@Override
	protected void initialize() {
		//subscribe TerminateBroadcast
		subscribeBroadcast(TerminateBroadcast.class,broad->{
			terminate();
		});

		//subscribe TickBroadcast
		subscribeBroadcast(TickBroadcast.class, broad->{
			Timetick = broad.getCurrTime();
		});

		//subscribe GadgetAvailableEvent
		subscribeEvent(GadgetAvailableEvent.class, message -> {
			Object[] answer={inventory.getItem(message.getGadget()),Timetick};;
			complete(message, answer);
		});
	}
}
