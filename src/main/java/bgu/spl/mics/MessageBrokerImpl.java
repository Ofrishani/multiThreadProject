package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> topic;
    private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> subscriberQueue;
    private ConcurrentHashMap<Event, Future> resultsMap;


    private MessageBrokerImpl() {
        topic = new ConcurrentHashMap<>();
        subscriberQueue = new ConcurrentHashMap<>();
        resultsMap = new ConcurrentHashMap<>();
    }

    private static class SingeltonHolder {
        private static MessageBrokerImpl single_instance = new MessageBrokerImpl();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {
        return SingeltonHolder.single_instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        topic.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        topic.get(type).add(m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        topic.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        topic.get(type).add(m);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        resultsMap.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        for (Subscriber subscriber : topic.get(b.getClass())) {
            subscriberQueue.get(subscriber).add(b);
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        ConcurrentLinkedQueue<Subscriber> subscribers = topic.get(e.getClass());
        if (subscribers == null) {
            return null;
        }
        Subscriber sub;

        synchronized (e.getClass()) {
            sub = subscribers.poll();
            if (sub == null) {
                return null;
            }
            subscribers.add(sub);
        }

        resultsMap.put(e, new Future<T>());
        synchronized (sub) {
            LinkedBlockingQueue<Message> q = subscriberQueue.get(sub);
            if (q != null)
                q.add(e);
        }
        return resultsMap.get(e);
    }

    @Override
    public void register(Subscriber m) {
        subscriberQueue.putIfAbsent(m, new LinkedBlockingQueue<>());
    }

    @Override
    public void unregister(Subscriber m) {
        //unsubscribe m
        topic.keySet().forEach(clazz -> {
            synchronized (clazz) {
                topic.get(clazz).remove(m);
            }
        });
        LinkedBlockingQueue<Message> q;
        //remove m from subscriber map
        synchronized (m) {
            q = subscriberQueue.remove(m);
        }
        //abort all m missions
        while (!(q.isEmpty())) {
            Message msg = q.poll();
            Future f = resultsMap.get(msg);
            if (f != null) {
                if (!(f.isDone())) {
                    f.resolve(null);
                }
            }
        }
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        if (!(subscriberQueue.containsKey(m))) {
            throw new IllegalStateException("Subscriber has never registered");
        }
        return subscriberQueue.get(m).take();

    }
}


