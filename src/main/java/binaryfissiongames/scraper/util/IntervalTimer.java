package binaryfissiongames.scraper.util;

// Used to time from when reset() was called to the given interval.
public class IntervalTimer {

    long intervalMs;
    long startTimestamp;

    public IntervalTimer(long intervalMs){
        this.intervalMs = intervalMs;
        this.startTimestamp = System.currentTimeMillis();
    }

    public void reset(){
        this.startTimestamp = System.currentTimeMillis();
    }

    public boolean isDone(){
        return this.startTimestamp < (System.currentTimeMillis() - intervalMs);
    }
}
