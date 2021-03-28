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

    /**
     *
     */
    public static class Builder {

        private DragQueryType queryType = null;
        private Map headers = null;
        private String postBody = null;
        private String queryString = null;

        /**
         *
         * @param queryType
         * @return
         */
        public Builder withQueryType(DragQueryType queryType) {
            this.queryType = queryType;
            return this;
        }

        /**
         *
         * @param headers
         * @return
         */
        public Builder withDragHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         *
         * @param postBody
         * @return
         */
        public Builder withDragBody(String postBody) {
            this.postBody = postBody;
            return this;
        }

        /**
         *
         * @param queryString
         * @return
         */
        public Builder withQuery(String queryString) {
            this.queryString = queryString;
            return this;
        }

        /**
         *
         * @return
         */
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

    /**
     *
     * @return
     */
    public DragQueryType getQueryType() {
        return queryType;
    }

    /**
     *
     * @return
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     *
     * @return
     */
    public String getPostBody() {
        return postBody;
    }

    /**
     *
     * @return
     */
    public String getQueryString() {
        return queryString;
    }

}
