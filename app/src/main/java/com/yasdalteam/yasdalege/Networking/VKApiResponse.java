package com.yasdalteam.yasdalege.Networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VKApiResponse
{
    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("error")
    @Expose
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {

        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }



    }

    public class RequestParam {

        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("value")
        @Expose
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public class Error {

        @SerializedName("error_code")
        @Expose
        private long errorCode;
        @SerializedName("error_msg")
        @Expose
        private String errorMsg;
        @SerializedName("request_params")
        @Expose
        private List<RequestParam> requestParams = null;

        public long getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(long errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public List<RequestParam> getRequestParams() {
            return requestParams;
        }

        public void setRequestParams(List<RequestParam> requestParams) {
            this.requestParams = requestParams;
        }

    }
}
