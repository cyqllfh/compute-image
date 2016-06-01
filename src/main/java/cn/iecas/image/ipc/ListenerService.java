package cn.iecas.image.ipc;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Properties;

/**
 * Created by chu on 16-5-31.
 */
public class ListenerService implements Runnable {

    private KafkaConsumer<String, String> consumer;

    private void loadKafkaProperties(Properties props) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("kafka.properties");
            props.load(is);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private PriorityBlockingQueue<Task> tasksQueue;

    public ListenerService(PriorityBlockingQueue<Task> tasksQueue, String topic) {
        this.tasksQueue = tasksQueue;
        Properties props = new Properties();
        loadKafkaProperties(props);
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("test-javaapi"));
    }

    private Task parseRecord(ConsumerRecord<String, String> record) throws JSONException {
        JSONObject jsonObject = new JSONObject(record.value());
        String op = jsonObject.getString("op");
        String input = jsonObject.getString("input");
        String output = jsonObject.getString("output");
        String params = jsonObject.getString("params");
        Task task = new Task(record.key(), op, input, output, params);
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
                //Thread.sleep(1000);
            }
        } catch(Exception e) {
            //ignore for shutdown
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
