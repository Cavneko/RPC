package com.wsw.netty.client;

import com.wsw.netty.util.Response;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    public final static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture = new ConcurrentHashMap<Long, DefaultFuture>();
    final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Response response;

    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(), this);

    }

    //主线程获取数据，首先要等待结果
    public Response get() {
        lock.lock();
        //System.out.println("DF:LOCKED");

        try {
            while (!done()){
                //System.out.println("DF:waiting for response");
                condition.await();
                //System.out.println("DF:await");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            //System.out.println("DF:unlocked");
        }
        //System.out.println("DF:returning response");
        return this.response;
    }

    public static void receive(Response response) {
        DefaultFuture df = allDefaultFuture.get(response.getId());

        if (df != null) {
            Lock lock = df.lock;
            lock.lock();

            try {
                df.setResponse(response);
                df.condition.signal();
                allDefaultFuture.remove(df);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private boolean done() {
        //System.out.println("DF: checking response");
        return this.response != null;
    }
}
