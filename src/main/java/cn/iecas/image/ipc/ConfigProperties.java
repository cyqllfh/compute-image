package cn.iecas.image.ipc;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chu on 16-6-2.
 */
class ConfigProperties {

    static Properties props;

    static Properties loadProperties(String filename) {
        props = new Properties();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(filename);
            props.load(is);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return props;
    }
}
