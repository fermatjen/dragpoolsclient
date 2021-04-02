# Simple Drag Pools Java Client
A simple Java-based client for Drag Pools to send Drag Queries with payload to a running Drag Pools cluster (https://dragpools.io).

Add the required Drag Pool client dependency in you pom.xml file:

```xml
        <dependency>
            <groupId>com.dragpools.client</groupId>
            <artifactId>DP_CLIENT</artifactId>
            <version>1.0.10</version>
        </dependency>
```

If your are on Gradle:

```
implementation 'com.dragpools.client:DP_CLIENT:1.0.10'
```

First, instantiate a Master:

```java
        DragPoolsMaster dragMaster = new DragPoolsMaster.Builder()
                                      .withHost("https://<DRAGPOOLS_MASTER_IP>")
                                      .withPort(443).build();
```

Then, create a Drag Query:

```java
       //Initialize header
       Map<String, String> dragHeaders = new LinkedHashMap<>();
       dragHeaders.put("Authorization", "Bearer <DRAG_BEARER_TOKEN>");

       //Simple Get Query
       DragQuery dragGetQuery = new DragQuery.Builder()
                                  .withQueryType(DragQueryType.GET)
                                  .withQuery("document/drag")
                                  .withDragHeaders(dragHeaders)
                                  .build();
```

Now, execute the Drag Query:

```java
        DragResult result = dragMaster.executeQuery(dragGetQuery);

        if (result.isSuccess()) {
            String response = result.getDragResult().toString();
            System.out.println(response);
            System.out.println("Time in ms: " + result.getQueryTime());
        } else {
            System.out.println(result.getErrorMessage());
        }
```

Similiarly, send a post request:

```java
        String postBody = "<SOME_JSON_PAYLOAD>";
        DragQuery dragPostQuery = new DragQuery.Builder()
                .withQueryType(DragQueryType.POST)
                .withQuery("<SOME_DRAG_POST_ENDPOINT>")
                .withDragBody(postBody)
                .withDragHeaders(dragHeaders)
                .build();

```

Let's say you want to store a KV pair and the value a simple JSON object representing an employee record. First, define the JSON object:

```java
        JSONObject emp = new JSONObject();
        emp.put("name", "jen");
        emp.put("age", 35);
        emp.put("city", "Chennai");
```
Now that you have a simple employee record, create a Drag Document:

```java
        DragDocument doc = new DragDocument();
        // Set the key and value
        doc.setKey("EMP_1");
        doc.setJsonValue(emp);
```
You have a Drag Document ready to be sent to the Master!

```java
        // Get the drag body from document
        String dragBody = doc.getDragBody();
        
        // Construct the Drag Query
        DragQuery dragPostQuery = new DragQuery.Builder()
                .withQueryType(DragQueryType.POST)
                .withQuery("/document")
                .withDragBody(dragBody)
                .withDragHeaders(dragHeaders)
                .build();
```

Now, hit the Master!

```java
        //Execute drag query
        DragResult result = dragMaster.executeQuery(dragPostQuery);
```

Let's create a Runner. Runners are simple Java code that can be broadcasted to all the available (healthy) Workers. Workers execute the Java code and send back the result.

Let's say we have a file `MyRunner.drn` containing the Runner code:

```java
package com.dragpools.runners;

import java.util.*;
import java.util.concurrent.*;

public class DragRunner {
    
    public Map init(){
        LinkedHashMap<String, String> initMap = new LinkedHashMap<>();
        
        initMap.put("mode", "live");
        initMap.put("name", "My Simple Runner");
        
        return initMap;
    }

    public Map run(ConcurrentNavigableMap<String, String> dragPoolData) {
        // We will wrap our compute results in a Map
        LinkedHashMap<String, Integer> myMap = new LinkedHashMap<>();
        
        myMap.put("Drag Pool Size", dragPoolData.size());
        
        return myMap;
    }
}
```

This is a very simple Runner to get the size of the KV store.

```java
        Path fileName = Path.of("MyRunner.drn");
        
        try {
            String runnerBody = Files.readString(fileName);

            //Define a DragRunner
            DragRunner runner = new DragRunner(runnerBody);

            DragQuery dragRunnerQuery = new DragQuery.Builder()
                    .withQueryType(DragQueryType.POST)
                    .withQuery("/runner")
                    .withDragRunner(runner)
                    .withDragHeaders(dragHeaders)
                    .build();

            //Execute drag query
            DragResult result = dragMaster.executeQuery(dragRunnerQuery);

            if (result.isSuccess()) {
                String response = result.getDragResult().toString();
                System.out.println(response);
                System.out.println("Time in ms: " + result.getQueryTime());
            } else {
                System.out.println(result.getErrorMessage());
            }

        } catch (IOException ex) {
            // <YOUR CODE>
        }
```

Similarly, instead of Runner the payload type could be a simple function that can be executed in the Master. Drag Functions are pure `JavaScript/TypeScript` that can be executed in the Master.

Let's say we have a JavaScript file `myjs.js`:

```javascript
var build = function() {
    var map = {};
    map.name = "MySimpleFunction";
    return map;
};

var run = function(map) {
    return "Hello from Master!";
};
```

Now, you can send this function as a payload:

```java
        Path fileName = Path.of("myjs.js");
        String functionBody = Files.readString(fileName);

        //Define a Drag Function
        DragFunction function = new DragFunction(functionBody);

        DragQuery dragFunctionQuery = new DragQuery.Builder()
                .withQueryType(DragQueryType.POST)
                .withQuery("/function")
                .withDragFunction(function)
                .withDragHeaders(dragHeaders)
                .build();
```

Let's send a piped-query to the Master that might trigger a long-running job. Drag Pool Jobs come handy when you can fetch the query results at your convenience. Note that depending on the Drag Pools cluster (number of Workers, hardware setup, healing Workers, busy Masters) jobs can finish immediately or take a few seconds to complete.

The following POST body defines our piped query:

```json
{
	"requests": ["/documents/name/contain:James","/documents/*/Rebecca"],
	"filters":"description/contain:huge"
}
```

Note that we initiate multiple searches across Workers and later assemble the results in the Master and do a final filtering. There are two Drag queries and they go to two available Workers. When both the Workers return the results, Master further filters using the condition mentioned.

Now, send the POST request:

```java
        // Construct the Drag Query
        DragQuery dragPostQuery = new DragQuery.Builder()
                .withQueryType(DragQueryType.POST)
                .withQuery("/job")
                // dragBody is your JSON JOB string
                .withDragBody(dragBody)
                .withDragHeaders(dragHeaders)
                .build();
```

Master returns a JOB ID that can be used to fetch the results:

```json
{
    "requests": [
        "/documents/name/contain:James",
        "/documents/*/Rebecca"
    ],
    "filters": "description/contain:huge",
    "id": "e529a7fe-beea-4940-b87b-5ef68d9dc28f",
    "status": "In progress",
    "result": []
}
```

To fetch the result of your Job based on the ID, create a simple Drag Query:

```java
       //Simple Get Query
       DragQuery dragGetQuery = new DragQuery.Builder()
                                  .withQueryType(DragQueryType.GET)
                                  .withQuery("job/e529a7fe-beea-4940-b87b-5ef68d9dc28f")
                                  .withDragHeaders(dragHeaders)
                                  .build();
```


See the [API Documentation](https://documenter.getpostman.com/view/5821672/RzZ7nzee) for more examples.
