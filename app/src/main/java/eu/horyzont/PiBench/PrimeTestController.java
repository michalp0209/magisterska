package eu.horyzont.PiBench;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;

public class PrimeTestController {
    private Queue<Integer> values;
    private LocalTime time;

    public PrimeTestController(int val){
        values = new LinkedList<>();
        switch(val){
            case 1279: {
                for(int i=2;i<=val;i++){
                    values.add(i);
                }
                break;
            }
            case 2281: {
                for(int i=1000;i<=val;i++){
                    values.add(i);
                }
                break;
            }
            case 3217: {
                for(int i=2000;i<=val;i++){
                    values.add(i);
                }
                break;
            }
            case 4253: {
                for(int i=3000;i<=val;i++){
                    values.add(i);
                }
                break;
            }
            case 9689: {
                for(int i=4000;i<=val;i++){
                    values.add(i);
                }
                break;
            }
        }
    }

    public synchronized int takeValue(){
        if(!values.isEmpty()) return values.remove();
        else return 0;
    }
    public synchronized void setTimer(){
        time = LocalTime.now();
    }
    public synchronized void setFinishTimer(){
        time = LocalTime.now().minusSeconds(time.toSecondOfDay());
    }
    public LocalTime getTime(){
        return time;
    }
}
