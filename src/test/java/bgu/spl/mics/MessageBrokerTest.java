package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.publishers.ExampleMessageSender;
import bgu.spl.mics.example.subscribers.ExampleEventHandlerSubscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBroker mes;
    intEvent event;
    Subscriber subscriber = new Q();
    Future<Integer> future;
    broadcastEvent broad;

    @BeforeEach
    public void setUp(){
        mes= MessageBrokerImpl.getInstance();
        subscriber = new Q();
        MissionInfo[] missions = new MissionInfo[2];
        broad = new broadcastEvent();
        event = new intEvent();
        future = new Future<>();
    }

    @Test
    public void subscribeEvent(){
        mes.register(subscriber);
        mes.subscribeEvent(event.getClass(),subscriber);
        mes.sendEvent(event);
        try {
             assertTrue(mes.awaitMessage(subscriber)==event);
        } catch (Exception e){
            fail("Test failed");
        }
        mes.unregister(subscriber);
    }

    @Test
    public void subscribeBroadcast(){
        mes.register(subscriber);
        mes.subscribeBroadcast(broad.getClass(),subscriber);
        mes.sendBroadcast(broad);
        try {
            assertSame(mes.awaitMessage(subscriber), broad);
        } catch (Exception e){
            fail("Test failed");
        }
        mes.unregister(subscriber);
    }

    @Test
    public void complete(){
        mes.register(subscriber);
        mes.subscribeEvent(event.getClass(),subscriber);
        future = mes.sendEvent(event);
        mes.complete(event,2);
        assertTrue(future.isDone());
        mes.unregister(subscriber);
    }

    @Test
    public void sendBroadcast(){
        Q sub1 = new Q();
        Q sub2 = new Q();
        mes.register(sub1);
        mes.register(sub2);
        mes.subscribeBroadcast(broad.getClass(),sub1);
        mes.subscribeBroadcast(broad.getClass(),sub2);
        mes.sendBroadcast(broad);
        try {
            assertTrue((mes.awaitMessage(sub1)==broad)&(mes.awaitMessage(sub2)==broad));
        } catch (Exception e){
            fail("Test failed");
        }
        finally {
            mes.unregister(sub1);
            mes.unregister(sub2);
        }
    }

    @Test
    public void sendEventNull(){
        assertNull(mes.sendEvent(event),"There are no subscribers in this event, so it should be null");
    }

    @Test
    public void sendEvent(){
        try {
            mes.register(subscriber);
            mes.subscribeEvent(event.getClass(), subscriber);
            future = mes.sendEvent(event);
            mes.complete(event, 5);
            if (future == null) {
                fail("We sent subscriber to this event so it should not be null");
            }
            if (future.get() != 5) {
                fail("We got wrong result (we expected 5");
            }
        }catch (InterruptedException ie){
            fail("Future::get throws an exception");
        }

        mes.unregister(subscriber);
    }

    @Test
    public void register() {
        try {
            mes.awaitMessage(subscriber);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException,"Error msg is wrong");
        }
    }

    @Test
    public void register1() {
        mes.register(subscriber);
        mes.subscribeEvent(event.getClass(),subscriber);
        mes.sendEvent(event);
        try {
            assertSame(mes.awaitMessage(subscriber), event);
        } catch (Exception e){
            if (e instanceof IllegalStateException){
                fail("subscriber has registered");
            } else {
                fail("subscriber interrupted");
            }
        }
        mes.unregister(subscriber);

    }

    @Test
    public void unregister(){
        mes.register(subscriber);
        mes.subscribeEvent(event.getClass(),subscriber);
        mes.sendEvent(event);
        mes.unregister(subscriber);
        try {
            if (mes.awaitMessage(subscriber)==event){
                fail("Subscriber is unregister so it should not get events in queue");
            }
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException,"Error msg is wrong");
        }
    }

    @Test
    public void awaitMessage(){
        try {
            mes.awaitMessage(subscriber);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

    @Test
    public void awaitMessage1() {
        mes.register(subscriber);
        mes.subscribeEvent(event.getClass(), subscriber);
        mes.sendEvent(event);

        try {
            assertSame(mes.awaitMessage(subscriber), event);
        } catch (Exception e) {
            if (e instanceof IllegalStateException) {
                fail("subscriber has registered");
            }
            if ((e instanceof InterruptedException)) {
                fail("subscriber interuped");
            } else {
                fail("There is something wrong..");
            }
        }
        mes.unregister(subscriber);
    }
}