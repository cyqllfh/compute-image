package cn.iecas.image.ipc;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chu on 16-6-2.
 */
class KafkaProperties {

    static Properties loadKafkaProperties() {
        Properties props = new Properties();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("kafka.properties");
            props.load(is);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return props;
    }
}
