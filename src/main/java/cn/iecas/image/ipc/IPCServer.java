package cn.iecas.image.ipc;

import java.util.concurrent.PriorityBlockingQueue;
/**
 * Created by chu on 16-5-31.
 */
public class IPCServer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage IPCServer topic!");
            System.exit(0);
        }
        PriorityBlockingQueue<Task> tasksQueue = new PriorityBlockingQueue<Task>();
        ListenerService listenerService = new ListenerService(tasksQueue, args[0]);
        Thread listener = new Thread(listenerService);
        listener.start();
        StarterService starterService = new StarterService(tasksQueue);
        Thread starter = new Thread(starterService);
        starter.start();
        try {
            listener.join();
            starter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
