# Simple Drag Pools Java Client
A simple Java-based client for Drag Pools to send Drag Queries with payload to a running Drag Pools cluster (https://dragpools.io).

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
        
        
