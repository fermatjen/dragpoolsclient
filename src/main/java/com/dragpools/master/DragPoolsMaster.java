package com.dragpools.master;

import com.dragpools.query.DragQuery;
import com.dragpools.query.DragQueryType;
import com.dragpools.result.DragResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Frank Jennings https://www.dragpools.io
 */
public class DragPoolsMaster {

    private String masterHost = null;
    private int masterPort = 12000;

    /**
     *
     */
    public static class Builder {

        private String masterHost = null;
        private int masterPort = 12000;

        /**
         *
         * @param masterHost
         * @return
         */
        public Builder withHost(String masterHost) {
            this.masterHost = masterHost;
            return this;
        }

        /**
         *
         * @param masterPort
         * @return
         */
        public Builder withPort(int masterPort) {
            this.masterPort = masterPort;
            return this;
        }

        /**
         *
         * @return
         */
        public DragPoolsMaster build() {

            DragPoolsMaster dpMaster = new DragPoolsMaster();
            dpMaster.masterHost = this.masterHost;
            dpMaster.masterPort = this.masterPort;

            return dpMaster;
        }

    }

    private DragPoolsMaster() {
        // Nothing here
    }

    /**
     *
     * @param result
     * @param errorMessage
     * @return
     */
    public DragResult interruptResult(DragResult result, String errorMessage) {

        result.setErrorMessage(errorMessage);
        result.setIsSuccess(false);

        return result;
    }

    /**
     *
     * @param result
     * @param jsonResponse
     * @param queryTime
     * @return
     */
    public DragResult propagateResult(DragResult result, String jsonResponse, long queryTime) {

        if (jsonResponse.startsWith("{") && jsonResponse.endsWith("}")) {
            
            JSONObject dragResponseObject = new JSONObject(jsonResponse);
            
            result.setDragResult(dragResponseObject);
            result.setQueryTime(queryTime);
            result.setIsSuccess(true);

        } else if (jsonResponse.startsWith("[") && jsonResponse.endsWith("]")) {
            
            JSONArray jsonResponseWrapper = new JSONArray(jsonResponse);
            JSONObject dragResponseObject = new JSONObject(jsonResponse);
            dragResponseObject.put("Result", jsonResponseWrapper);
            
            result.setDragResult(dragResponseObject);
            result.setQueryTime(queryTime);
            result.setIsSuccess(true);

        } else {
            result.setDragResult(null);
            result.setIsSuccess(false);

            result.setErrorMessage("Invalid Drag Query or insufficient Drag Pool Permissions!");

        }

        return result;
    }

    /**
     *
     * @param query
     * @return
     */
    public DragResult executeQuery(DragQuery query) {

        DragResult result = new DragResult();
        DragQueryType queryType = query.getQueryType();

        URL url = null;

        try {
            String queryString = query.getQueryString();
            
            if(queryString.startsWith("/")){
                queryString = queryString.substring(1, queryString.length());
            }
            url = new URL(masterHost + ":" + masterPort + "/" + queryString);
        } catch (MalformedURLException ex) {
            return interruptResult(result, ex.getMessage());
        }

        StringBuilder content = new StringBuilder();

        HttpURLConnection connection = null;
        
        long start = 0L;
        long end = 0L;
        
        start = System.currentTimeMillis();

        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            return interruptResult(result, ex.getMessage());
        }

        //Master only sends JSON
        connection.setRequestProperty("accept", "application/json");
        //Add other headers
        Map<String, String> headersMap = query.getHeaders();

        for (String key : headersMap.keySet()) {
            String value = headersMap.get(key);
            connection.setRequestProperty(key, value);
        }

        switch (queryType) {
            case GET -> {
                try {
                    connection.setRequestMethod("GET");
                } catch (ProtocolException ex) {
                    return interruptResult(result, ex.getMessage());
                }
            }
            case POST -> {
                try {
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    //Write body
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = query.getPostBody().getBytes("utf-8");
                        os.write(input, 0, input.length);
                    } catch (IOException ex) {
                        return interruptResult(result, ex.getMessage());
                    }
                } catch (ProtocolException ex) {
                    return interruptResult(result, ex.getMessage());
                }
            }
            default -> {
                return interruptResult(result, "The request method is not supported! Allowed are GET and POST only.");
            }

        }

        //Process response
        try {

           
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }

        } catch (MalformedURLException ex) {
            return interruptResult(result, ex.getMessage());

        } catch (IOException ex) {
            return interruptResult(result, ex.getMessage());
        }

        String r = content.toString().trim();
        
        end = System.currentTimeMillis();
        
        return propagateResult(result, r, (end - start));
        
    }
}
