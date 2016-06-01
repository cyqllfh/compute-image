package cn.iecas.image.ipc;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Collections;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Properties;

/**
 * Created by chu on 16-5-31.
 */
public class ListenerService implements Runnable {

    private static Properties props;
    private KafkaConsumer<String, String> consumer;

    static {
        props = new Properties();
        props.put("bootstrap.servers", "slave1:9092,slave2:9092,slave3:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }

    private PriorityBlockingQueue<Task> tasksQueue;

    public ListenerService(PriorityBlockingQueue<Task> tasksQueue, String topic) {
        this.tasksQueue = tasksQueue;
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("test-javaapi"));
    }

    private Task parseRecord(ConsumerRecord<String, String> record) {
        String[] taskList = record.value().split("|");
        Task task = new Task(record.key(), taskList[0], taskList[1], taskList[2],taskList[3]);
        return task;
    }

    public void run() {
        try {
            while (true) {
                System.out.println("LLLLLLLLLLLLLLLLLLLs im am in listener!");
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                    Task task = parseRecord(record);
                    tasksQueue.put(task);
                }
                Thread.sleep(1000);
            }
        } catch(Exception e) {
            //ignore for shutdown
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
