package com.common;

import org.springframework.util.StopWatch;

public class StopWatchUtil {

    private StopWatch stopWatch;

    public StopWatchUtil() {
        this("");
    }

    public StopWatchUtil(String id) {
        stopWatch = new StopWatch(id);
    }

    public String getId() {
        return stopWatch.getId();
    }

    public void setKeepTaskList(boolean keepTaskList) {
        stopWatch.setKeepTaskList(keepTaskList);
    }

    public void start() throws IllegalStateException {
        start("");
    }

    public void start(String taskName) throws IllegalStateException {
        if (isRunning())
            stop();
        stopWatch.start(taskName);
    }

    public void stop() throws IllegalStateException {
        if (isRunning())
            stopWatch.stop();
    }

    public boolean isRunning() {
        return stopWatch.isRunning();
    }

    private String currentTaskName() {
        return stopWatch.currentTaskName();
    }

    public long getLastTaskTimeMillis() throws IllegalStateException {
        return stopWatch.getLastTaskTimeMillis();
    }

    public String getLastTaskName() throws IllegalStateException {
        return stopWatch.getLastTaskName();
    }

    public StopWatch.TaskInfo getLastTaskInfo() throws IllegalStateException {
        return stopWatch.getLastTaskInfo();
    }

    public long getTotalTimeMillis() {
        return stopWatch.getTotalTimeMillis();
    }

    public double getTotalTimeSeconds() {
        return stopWatch.getTotalTimeSeconds();
    }

    public double getTaskCount() {
        return stopWatch.getTaskCount();
    }

    public StopWatch.TaskInfo[] getTaskInfo() {
        return stopWatch.getTaskInfo();
    }

    public String shortSummary() {
        return stopWatch.shortSummary();
    }

    public String prettyPrint() {
        return stopWatch.prettyPrint();
    }

    @Override
    public String toString() {
        return stopWatch.toString();
    }

}
