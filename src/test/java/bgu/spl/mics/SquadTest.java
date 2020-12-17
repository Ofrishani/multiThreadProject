package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {
    Squad s;
    Agent[] agent ;
    @BeforeEach
    public void setUp(){
        s = Squad.getInstance();
        agent = new Agent[2];
        Agent a = new Agent();
        a.setName("x");
        a.setSerialNumber("001");
        Agent b = new Agent();
        b.setName("y");
        b.setSerialNumber("002");
        agent[0] = a;
        agent[1]=b;
        s.load(agent);
    }

    @Test
    void getInstance() {
        assertTrue(Squad.getInstance()==Squad.getInstance());
    }

    @Test
    void load1() {
        Agent a = new Agent();
        a.setName("a");
        a.setSerialNumber("003");
        Agent b = new Agent();
        b.setName("b");
        b.setSerialNumber("005");
        Agent[] agent = {a,b};
        s.load(agent);
        List<String> check = new LinkedList<>();
        check.add("003");
        check.add("005");
        assertTrue(s.getAgents(check));
    }

    @Test
    void releaseAgents() {
        List<String> serials = new LinkedList<>();
        serials.add("001");
        s.releaseAgents(serials);
        assertTrue(agent[0].isAvailable());
    }

    @Test
    void sendAgents() {
        try{
            List<String> serials = new LinkedList<>();
            serials.add("001");
            s.sendAgents(serials, 10);
            assertTrue(agent[0].isAvailable());
        }catch (Exception ex) {
            fail("throws an exception");
        }
    }

    @Test
    void getAgents1() {
        List<String> check = new LinkedList<>();
        check.add("001");
        check.add("002");
        assertTrue(s.getAgents(check));
    }

    @Test
    void getAgents2() {
        List<String> check = new LinkedList<>();
        check.add("006");
        check.add("008");
        assertTrue(!s.getAgents(check));
    }

    @Test
    void getAgentsNames() {
        List<String> serials = new LinkedList<>();
        serials.add("001");
        serials.add("002");
        List<String> check = new LinkedList<>();
        check.add("x");
        check.add("y");
        List<String> out = s.getAgentsNames(serials);
        assertTrue(out.equals(check));
    }

}
