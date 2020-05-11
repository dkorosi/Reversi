package gui;

public class Timer implements java.lang.Runnable {
    private int time;
    private boolean stop;

    public Timer(int time) {
        this.stop = false;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void run() {
        stop = false;

        while (true) {
            // System.out.println(time);
            if (stop) {
                break;
            }

            try {
                if (0 != time) {
                    time--;
                    Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public void stop() {
        this.stop = true;
    }

}



