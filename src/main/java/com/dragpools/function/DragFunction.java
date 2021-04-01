package com.dragpools.function;

/**
 *
 * @author Frank Jennings https://www.dragpools.io
 */
public class DragFunction {

    private String functionBody = null;

    public DragFunction() {

    }

    public DragFunction(String functionBody) {
        this.functionBody = functionBody;
    }

    public String getFunctionBody() {
        return functionBody;
    }

    public void setFunctionBody(String functionBody) {
        this.functionBody = functionBody;
    }

}
