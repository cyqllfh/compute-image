package cn.iecas.image.ipc;

import java.util.concurrent.PriorityBlockingQueue;
/**
 * Created by chu on 16-5-31.
 */
public class IPCServer {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage IPCServer tasktopic acktopic!");
            System.exit(0);
        }
        PriorityBlockingQueue<Task> tasksQueue = new PriorityBlockingQueue<Task>();
        TaskAccepter taskAccepter = new TaskAccepter(tasksQueue, args[0]);
        Thread accepter = new Thread(taskAccepter);
        accepter.start();
        TaskDealer taskDealer = new TaskDealer(tasksQueue, args[1], Integer.parseInt(args[2]));
        taskDealer.start();
        try {
            accepter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
