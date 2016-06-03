package cn.iecas.image.ipc;

/**
 * Created by chu on 16-6-3.
 */
public class TestThread extends Thread{
    public TestThread(String name) {
        super(name);
    }

    public void run() {
        for(int i = 0;i<50;i++){
            //for(long k= 0; k <100000000;k++);
            System.out.println(this.getName()+" :"+i);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new TestThread("阿三");
        Thread t2 = new TestThread("李四");
        t1.start();
        t2.start();
    }
}