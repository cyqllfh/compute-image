package cn.iecas.image.ipc;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import cn.iecas.image.compute.Client;

/**
 * Created by chu on 16-5-31.
 */
public class StarterService implements Runnable{
    private PriorityBlockingQueue<Task> taskQueue;

    // max applications runing on yarn at the same time
    final int EXECUTE_QUEUE_SIZE = 10;

    public StarterService(PriorityBlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void execute(Task task) {
        String[] params = {"-jar", "target/compute-on-yarn-1.0.jar",
                "-shell_command", "hadoop fs -put /var/log/hadoop-yarn/hehe /chu",
                "-num_containers", "1",
                "-container_memory", "350",
                "-master_memory", "350",
                "-priority", "10"};
        Client.start(params);
    }

    public void run() {

        while(true) {
            try {
                System.out.println("SSSSSSSSSSSSSSSSSSSSSs im am in start!");
                Task task = taskQueue.take();
                execute(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
