# Simple Drag Pools Java Client
A simple Java-based client for Drag Pools to send Drag Queries with payload to a running Drag Pools cluster (https://dragpools.io).

Add the required Drag Pool client dependency in you pom.xml file:

```xml
        <dependency>
            <groupId>com.dragpools.client</groupId>
            <artifactId>DP_CLIENT</artifactId>
            <version>1.0.9</version>
        </dependency>
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
       Map dragHeaders = new LinkedHashMap();
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
        
