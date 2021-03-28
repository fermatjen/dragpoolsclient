package com.dragpools.result;

import org.json.JSONObject;

/**
 *
 * @author Frank Jennings
 * https://www.dragpools.io
 */
public class DragResult {
    
    private JSONObject dragResult = null;
    private boolean isSuccess = false;
    private String errorMessage = null;
    private long queryTime = 0L;

    public DragResult() {
    }

    public JSONObject getDragResult() {
        return dragResult;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setDragResult(JSONObject dragResult) {
        this.dragResult = dragResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }
    
    
    
    
}
