package cn.iecas.image.ipc;

import java.util.Comparator;

/**
 * Created by chu on 16-5-31.
 */
public class Task implements Comparable<Task>{
    private String id;
    private String op;
    private String inPath;
    private String outPath;
    private String params;

    public Task(String id, String op, String inPath,
                String outPath, String params) {
        this.id = id;
        this.op = op;
        this.inPath = inPath;
        this.outPath = outPath;
        this.params = params;

    }

    public String getId() {
        return id;
    }

    public int compareTo(Task task) {
        return 0;
    }
}
