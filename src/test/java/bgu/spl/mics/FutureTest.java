package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;

public class FutureTest {
    Future<Integer> future;
    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    void get() {
        try {
            future.resolve(7);
            assertTrue(future.get() == 7);

        }catch (Exception ex) {
            fail("throws an exception");}
    }

    @Test
    void resolve() {
        try{
            future.resolve(4);
            assertTrue(future.get()==4);
        } catch (Exception ex){
            fail("throws an exception");
        }

    }

    @Test
    void isDone1() {
        assertTrue(!future.isDone());
    }

    @Test
    void isDone2() {
        future.resolve(6);
        assertTrue(future.isDone());
    }

    @Test
    void get1() {
        try {
            future.get(100,TimeUnit.MILLISECONDS);
            future.resolve(23);
            assertTrue(future.get()==23);
        }catch (Exception ex){
            fail("throws an exception");
        }
    }
}
