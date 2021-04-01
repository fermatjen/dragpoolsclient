package com.dragpools.runner;

/**
 *
 * @author Frank Jennings https://www.dragpools.io
 */
public class DragRunner {

    private String runnerBody = null;

    public DragRunner() {

    }

    public DragRunner(String runnerBody) {
        this.runnerBody = runnerBody;
    }

    public String getRunnerBody() {
        return runnerBody;
    }

    public void setRunnerBody(String runnerBody) {
        this.runnerBody = runnerBody;
    }

}
