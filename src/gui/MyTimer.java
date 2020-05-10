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


        while (time >=0) {
            System.out.println(String.valueOf(time) + " seconds remaining");
            if (stop ||time == 0) {
                break;
            }

            try {
                time--;
                Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
    }
    public void stop(){
    this.stop = true;
    }


}



