package com.dragpools.document;

import org.json.JSONObject;

/**
 *
 * @author Frank Jennings https://www.dragpools.io
 */
public class DragDocument {

    private String key = null;
    private JSONObject jsonValue = null;
    private String stringValue = null;

    public DragDocument() {

    }

    public DragDocument(String key, JSONObject jo) {
        this.key = key;
        this.jsonValue = jo;
    }

    public DragDocument(String key, String value) {
        this.key = key;
        this.stringValue = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JSONObject getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(JSONObject jsonValue) {
        this.jsonValue = jsonValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getDragBody() {

        JSONObject payload = null;

        if (jsonValue != null) {

            payload = new JSONObject();
            payload.put("key", key);
            payload.put("value", jsonValue);

            return payload.toString();

        } else if (stringValue != null) {

            payload = new JSONObject();
            payload.put("key", key);
            payload.put("value", stringValue);

            return payload.toString();
        } else {
            return null;
        }
    }

}
