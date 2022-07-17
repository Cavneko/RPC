package com.wsw.client.core;

import com.wsw.client.param.ClientRequest;
import com.wsw.client.param.Response;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    public final static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture = new ConcurrentHashMap<Long, DefaultFuture>();
    final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Response response;
    private long timeout = Long.valueOf(2*60*1000);
    private long startTime = System.currentTimeMillis();

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getStartTime() {
        return startTime;
    }

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

    public Response get(long time) {
        lock.lock();
        //System.out.println("DF:LOCKED");

        try {
            while (!done()){
                //System.out.println("DF:waiting for response");
                condition.await(time, TimeUnit.SECONDS);
                if ((System.currentTimeMillis() - startTime) > time) {
                    System.out.println("请求超时");
                    break;
                }
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


    static class FutureThread extends Thread {
        @Override
        public void run() {
            Set<Long> ids = allDefaultFuture.keySet();
            for (Long id : ids) {
                DefaultFuture df = allDefaultFuture.get(id);
                if (df == null) {
                    allDefaultFuture.remove(df);
                } else {
                    //假如链路超时
                    if (df.getTimeout() < (System.currentTimeMillis() - df.getStartTime())) {
                        Response resp = new Response();
                        resp.setId(id);
                        resp.setCode("333333");
                        resp.setMsg("链路请求超时");
                        receive(resp);
                    }
                }
            }
        }
    }

    static {
        FutureThread futureThread = new FutureThread();
        futureThread.setDaemon(true);
        futureThread.start();
    }

}

