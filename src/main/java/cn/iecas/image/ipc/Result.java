package cn.iecas.image.ipc;

/**
 * Created by chu on 16-6-3.
 */
public class Result {
    private String id;
    private int status;
    public Result(String id, int status) {
        this.id = id;
        this.status = status;
    }
    public String getId() {
        return id;
    }
    public int getStatus() {
        return status;
    }
}
