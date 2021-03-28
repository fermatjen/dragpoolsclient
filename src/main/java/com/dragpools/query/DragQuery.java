package com.dragpools.query;

import java.util.Map;

/**
 *
 * @author Frank Jennings
 * https://www.dragpools.io
 */
public class DragQuery {

    private DragQueryType queryType = null;
    private Map<String, String> headers = null;
    private String postBody = null;
    private String queryString = null;

    public static class Builder {

        private DragQueryType queryType = null;
        private Map headers = null;
        private String postBody = null;
        private String queryString = null;

        public Builder withQueryType(DragQueryType queryType) {
            this.queryType = queryType;
            return this;
        }

        public Builder withDragHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder withDragBody(String postBody) {
            this.postBody = postBody;
            return this;
        }
        public Builder withQuery(String queryString) {
            this.queryString = queryString;
            return this;
        }

        public DragQuery build() {

            DragQuery dQuery = new DragQuery();
            dQuery.queryType = this.queryType;
            dQuery.headers = this.headers;
            dQuery.queryString = this.queryString;
            dQuery.postBody = this.postBody;

            return dQuery;
        }

    }

    private DragQuery() {

    }

    public DragQueryType getQueryType() {
        return queryType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getPostBody() {
        return postBody;
    }

    public String getQueryString() {
        return queryString;
    }

}
