package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {

    static private String[] inventory;
    static private Agent[] squad;
    static private Intell[] intell;
    static private int moneypenny;
    static private int m;
    static private int time;
    static private List<Thread> threads = new LinkedList<>();
    static public CountDownLatch letch;

    public static void main(String[] args) {
int g=9;
        //read from JSON
        BufferedReader file;
        try {
            file = new BufferedReader(new FileReader(args[0]));
            Gson gson = new Gson();
            readFromJson read = gson.fromJson(file, readFromJson.class);
            inventory = read.inventory;
            squad = read.squad;
            intell = read.services.intelligence;
            moneypenny = read.services.Moneypenny;
            m = read.services.M;
            time = read.services.time;

            //get Instances
            Inventory inventoryInstance = Inventory.getInstance();
            inventoryInstance.load(inventory);
            Squad squadInstance = Squad.getInstance();
            squadInstance.load(squad);

            //create threads
            Thread timeService = new Thread(new TimeService(time));
            createIntelThread();
            createMoneyPennyThread();
            createMThread();
            Thread Q = new Thread(new Q(), "Q");
            threads.add(Q);
            letch = new CountDownLatch(threads.size());


            //start threads
            for (Thread s : threads) {
                s.start();
            }
            letch.await();

            threads.add(timeService);
            timeService.start();

            timeService.join();
            for (Thread thread : threads) {
                thread.join();
            }

            //print output
            Inventory.getInstance().printToFile(args[1]);
            Diary.getInstance().printToFile(args[2]);



        } catch (FileNotFoundException | InterruptedException ignored) {}
    }

    private static class readFromJson {
        private Services services;
        private String[] inventory;
        private Agent[] squad;
    }

    private class Services {
        private int M;
        private int Moneypenny;
        private Intell[] intelligence;
        private int time;
    }

    private class Intell {
        private MissionInfo[] missions;
    }

    /**
     * create intelligence Thread
     */
    public static void createIntelThread() {
        for (int i = 0; i < intell.length; i++) {
            Intelligence intelligence = new Intelligence(i + 1, intell[i].missions);
            Thread intellegence = new Thread(intelligence, intelligence.getName());
            threads.add(intellegence);
        }
    }

    /**
     * create M Thread
     */
    public static void createMThread() {
        for (int i = 0; i < m; i++) {
            M m = new M(i + 1);
            Thread M = new Thread(m, m.getName());
            threads.add(M);
        }
    }

    /**
     * create money penny Thread
     */
    public static void createMoneyPennyThread() {
        for (int i = 0; i < moneypenny; i++) {
            Moneypenny mp = new Moneypenny(i + 1);
            Thread MoneyPenny = new Thread(mp, mp.getName());
            threads.add(MoneyPenny);
        }
    }
}


