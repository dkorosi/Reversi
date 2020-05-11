package gui;

public class MyTimer implements java.lang.Runnable{
int time;
boolean stop;

    public MyTimer(int d)
    {
    this.stop = false;
    this.time = d;
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
                if(time != 0) {
                    time--;
                    Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
                }
                else
                    time = this.time;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
    }
    public void stop(){
    this.stop = true;
    }


}



