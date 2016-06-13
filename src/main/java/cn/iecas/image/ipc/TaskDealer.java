package cn.iecas.image.ipc;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by chu on 16-5-31.
 */
public class TaskDealer {

    private PriorityBlockingQueue<Task> taskQueue;
    private Queue<Result> resultQueue;
    private Producer<String, String> producer;
    private String ackTopic;
    private ExecutorService executorService;

    public TaskDealer(PriorityBlockingQueue<Task> taskQueue, String ackTopic, int taskNums) {
        this.taskQueue = taskQueue;
        this.producer = new KafkaProducer<String, String>(ConfigProperties.loadProperties("kafka.properties"));
        this.resultQueue = new LinkedList<Result>();
        this.ackTopic = ackTopic;
        //线程池 初始化十个线程 和JDBC连接池是一个意思 实现重用
        executorService = Executors.newFixedThreadPool(taskNums);
    }

    class Acker implements Runnable{
        private Queue<Result> resultQueue;
        public Acker(Queue<Result> resultQueue) {
            this.resultQueue = resultQueue;
        }
        public void run() {
            Result result;
            while(true) {
                System.out.println("AAAAAAAAAAAAAAs im am in acker!");
                synchronized (resultQueue) {
                    if(resultQueue.isEmpty()) {
                        try {
                            resultQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    result = resultQueue.poll();
                }
                sendResult(result);
            }
        }
    }


    private void sendResult(Result result) {
        String message = "{id:" + result.getId() + ", result:" + result.getStatus() + "}";
        producer.send(new ProducerRecord<String, String>(ackTopic, message));
    }

    public void start() {

        Thread ackerThread = new Thread(new Acker(resultQueue));
        ackerThread.start();
        while(true) {
            try {
                System.out.println("SSSSSSSSSSSSSSSSSSSSSs im am in dealer!");
                Task task = taskQueue.take();
                executorService.execute(new TaskSubmitter(task, resultQueue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
