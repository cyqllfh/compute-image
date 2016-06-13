package cn.iecas.image.ipc;

import cn.iecas.image.compute.Client;


import java.util.*;


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

    private List<String> getClientParams() {
        Properties props = ConfigProperties.loadProperties("container.properties");
        String command = props.getProperty("-location") + task.getOp() +
                " " + task.getInPath() + " " + task.getOutPath() + " " + task.getParams();
        props.setProperty("-shell_command", command);
        List<String> paramsList= new ArrayList<String>();
        Enumeration en = props.propertyNames();
        while(en.hasMoreElements()) {
            String key = en.nextElement().toString();
            if(!key.equals("-location")) {
                paramsList.add(key);
                paramsList.add(props.getProperty(key));
            }
        }
        return paramsList;
    }

    public void run() {

        //        String[] params = {"-jar", "target/compute-on-yarn-1.0.jar",
//                "-shell_command", "hadoop fs -put /var/log/hadoop-yarn/hehe /chu",
//                "-num_containers", "1",
//                "-container_memory", "350",
//                "-master_memory", "350",
//                "-priority", "10"};
        System.out.println("TTTTTTTTTTTs im am in submitter!");
//        String[] params = {"-jar", "target/compute-on-yarn-1.0.jar",
//                "-shell_script","/home/chu/Desktop/test.sh",
//                "-num_containers", "1",
//                "-container_memory", "350",
//                "-master_memory", "350",
//                "-priority", "10"};
        List<String> paramsList = getClientParams();
        String[] params = paramsList.toArray(new String[paramsList.size()]);
        int status = Client.start(params);
        Result result = new Result(task.getId(), status);
        synchronized (resultQueue) {
            resultQueue.add(result);
            resultQueue.notify();
        }
    }
}
