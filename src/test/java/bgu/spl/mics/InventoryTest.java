package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    Inventory inv;
    String[] gadgets;

    @BeforeEach
    public void setUp(){
        inv = Inventory.getInstance();
        gadgets=new String[2];
        gadgets[0]=("gun1");
        gadgets[1]=("camera");
        inv.load(gadgets);
    }

    @Test
    void getInstance() {
        assertTrue(inv==Inventory.getInstance());
    }

    @Test
    void load() {
        gadgets=new String[1];
        gadgets[0]=("gun2");
        inv.load(gadgets);
        assertTrue(inv.getItem("gun2"));

    }

    @Test
    void getItem1() {
        assertTrue(!inv.getItem("gun2"));
    }

    @Test
    void getItem2() {
        assertTrue(inv.getItem("gun1"));
    }

    @Test
    void printToFile() {//no need
    }
}
