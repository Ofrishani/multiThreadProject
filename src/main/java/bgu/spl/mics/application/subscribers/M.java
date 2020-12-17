package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class
M extends Subscriber {
    int TimeTick;
    int serialNumber;
    Diary diary;

    public M(int serialNumber) {
        super("M" + serialNumber);
        this.serialNumber = serialNumber;
        this.diary = Diary.getInstance();
    }

    @Override
    protected void initialize() {
        //subscribe TickBroadcast
        subscribeBroadcast(TickBroadcast.class, broad -> {
            TimeTick = broad.getCurrTime();
        });

        //subscribe TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, broad -> {
            terminate();
        });

        //subscribe MissionReceivedEvent
        subscribeEvent(MissionReceivedEvent.class, message -> {
            diary.incrementTotal();
            MissionInfo mission = message.getMission();
            AgentsAvailableEvent eventMP = new AgentsAvailableEvent(mission.getSerialAgentsNumbers());

            Future<Object[]> futureAAE = getSimplePublisher().sendEvent(eventMP);
            //Verify MP didnt do unregister and mission has not aborted. if so, we wont continue.
            if (futureAAE != null && futureAAE.get() != null) {
                Object[] resultAAE = futureAAE.get();
                ////check if needed agents are available.
                if ((boolean) resultAAE[0]) {
                    Future<Object[]> futureGAE = getSimplePublisher().sendEvent(new GadgetAvailableEvent(mission.getGadget()));
                    //Verify Q didnt do unregister and mission has not aborted. if so, we wont continue.
                    if (futureGAE != null && futureGAE.get() != null) {
                        Object[] resultGAE = futureGAE.get();
                        //check if needed gadget is available.
                        if ((boolean) resultGAE[0]) {
                            //check if time expired
                            if (TimeTick <= mission.getTimeExpired()) {
                                eventMP.getMp_whatToDo().resolve(message.getMission().getDuration());
                                createReport(message.getMission(), resultAAE, resultGAE);
                                return;
                            }
                        }
                    }
                }
            }
            eventMP.getMp_whatToDo().resolve(-1);
        });


    }

    private void createReport(MissionInfo mission, Object[] agentResult, Object[] gadgetResult) {
        Report report = new Report();
        report.setMissionName(mission.getMissionName());
        report.setAgentsSerialNumbers(mission.getSerialAgentsNumbers());
        report.setGadgetName(mission.getGadget());
        report.setM(serialNumber);
        report.setMoneypenny((int) agentResult[1]);
        report.setAgentsNames((List<String>) agentResult[2]);
        report.setQTime((int) gadgetResult[1]);
        report.setTimeIssued(mission.getTimeIssued());
        report.setTimeCreated(TimeTick);
        diary.addReport(report);
    }
}
