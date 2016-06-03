package cn.iecas.image.ipc;

import cn.iecas.image.compute.Client;
import com.sun.org.apache.regexp.internal.RE;

import java.util.Queue;
import java.util.concurrent.Callable;

/**
 * Created by chu on 16-6-3.
 */
public class TaskSubmitter implements Runnable{
    private Task task;
    private Queue<Result> resultQueue;

    public TaskSubmitter(Task task, Queue<Result> resultQueue) {
        this.task = task;
        this.resultQueue = resultQueue;
    }

    public void run() {

        //        String[] params = {"-jar", "target/compute-on-yarn-1.0.jar",
//                "-shell_command", "hadoop fs -put /var/log/hadoop-yarn/hehe /chu",
//                "-num_containers", "1",
//                "-container_memory", "350",
//                "-master_memory", "350",
//                "-priority", "10"};
        System.out.println("TTTTTTTTTTTs im am in submitter!");
        String[] params = {"-jar", "target/compute-on-yarn-1.0.jar",
                "-shell_script","/home/chu/Desktop/test.sh",
                "-num_containers", "1",
                "-container_memory", "350",
                "-master_memory", "350",
                "-priority", "10"};
        int status = Client.start(params);
        Result result = new Result(task.getId(), status);
        synchronized (resultQueue) {
            resultQueue.add(result);
            resultQueue.notify();
        }
    }
}
