/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.kubeforce.payroll.response.model;

import java.util.Map;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author samueladebowale
 */
@ToString
@RequiredArgsConstructor
public class AppResponse {

    @JsonProperty("metadata")
    private final AppResponseMetadata metadata;

    @JsonProperty("response")
    private final ApiResponse apiResponse;

    public AppResponse() {
        this.metadata = new AppResponseMetadata();
        this.apiResponse = null;
    }

    /**
     * @param apiVersion
     * @param message
     * @param apiError
     * @param sendReportUri
     * @param status
     * @param moreInfoUrl
     */
    public AppResponse(String apiVersion, String message,
            ApiError apiError, String sendReportUri,
            String status, String moreInfoUrl) {
        super();
        this.apiResponse = apiError;
        this.metadata = AppResponseMetadata.builder().apiVersion(apiVersion).status(status)
                .moreInfo(moreInfoUrl).sendReport(sendReportUri + "?id=" + apiError.getTraceId())
                .shortMessage(message).build();
    }

    /**
     * @param apiVersion
     * @param message
     * @param apiMessage
     * @param status
     * @param moreInfoUrl
     */
    public AppResponse(String apiVersion, String message,
            ApiMessage apiMessage, String status,
            String moreInfoUrl) {
        super();
        this.apiResponse = apiMessage;
        this.metadata = AppResponseMetadata.builder().apiVersion(apiVersion).status(status)
                .moreInfo(moreInfoUrl).shortMessage(message).build();
    }

    /**
     *
     * @param apiVersion
     * @param httpMethod
     * @param errorCode
     * @param code
     * @param message
     * @param domain
     * @param reason
     * @param errorReportUri
     * @param moreInfoUrl
     */
    public AppResponse(final String apiVersion, String httpMethod, String errorCode, final int code,
            final String message, final String domain, final String reason, final String errorReportUri,
            final String moreInfoUrl) {

        String status = HttpStatus.valueOf(code).name();

        //new ApiError(path, reqMethod, errorCode, httpCode, message, detailMessage);
        this.apiResponse = new ApiError(domain, httpMethod, errorCode, code, message, null);
        
        this.metadata = AppResponseMetadata.builder().apiVersion(apiVersion).status(status)
                .moreInfo(moreInfoUrl).sendReport(errorReportUri + "?id=" + this.apiResponse.getTraceId())
                .shortMessage(message)
                .build();
    }

    /**
     *
     * @param currentApiVersion
     * @param httpMethod
     * @param errorCode
     * @param httpStatus
     * @param message
     * @param path
     * @param sendReportUri
     * @param moreInfoUrl
     * @param ex
     */
    public AppResponse(String currentApiVersion, String httpMethod, String errorCode, HttpStatus httpStatus,
            String message, String path, String sendReportUri, String moreInfoUrl, Exception ex) {
        // TODO Auto-generated constructor stub
        super();
        this.apiResponse = new ApiError(path, httpMethod, errorCode, httpStatus.value(), message, ex.getMessage());
        this.metadata = AppResponseMetadata.builder().apiVersion(currentApiVersion).status(httpStatus.name())
                .moreInfo(moreInfoUrl).sendReport(sendReportUri + "?id=" + this.apiResponse.getTraceId())
                .shortMessage(message).build();
    }

    public static AppResponse fromDefaultAttributeMap(final String apiVersion, String httpMethod, String errorCode,
            final Map<String, Object> defaultErrorAttributes, final String sendReportBaseUri,
            final String moreInfoUrl) {

        // original attribute values are documented in
        // org.springframework.boot.web.servlet.error.DefaultErrorAttributes
        return new AppResponse(apiVersion, httpMethod, errorCode, ((Integer) defaultErrorAttributes.get("status")),
                (String) defaultErrorAttributes.getOrDefault("message", "no message available"),
                (String) defaultErrorAttributes.getOrDefault("path", "no domain available"),
                (String) defaultErrorAttributes.getOrDefault("error", "no reason available"),
                sendReportBaseUri, moreInfoUrl);
    }

    // utility method to return a map of serialized root attributes,
    // see the last part of the guide for more details
    public Map<String, Object> toAttributeMap() {
        return Map.of("meta", metadata, "response", apiResponse);
    }

    public ApiResponse getApiResponse() {
        // TODO Auto-generated method stub
        return this.apiResponse;
    }

}
