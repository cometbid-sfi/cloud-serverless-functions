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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Data;
import org.apache.logging.log4j.ThreadContext;
import org.cometbid.kubeforce.payroll.common.util.TimeZonesConverter;
import org.springframework.http.HttpStatus;

/**
 *
 * @author samueladebowale
 */
@Data
public abstract class ApiResponse {

    /**
     * Url of request that produced the error.
     */
    @JsonProperty(value = "path")
    protected String path = "Not available";

    /**
     * Short, human-readable summary of the problem.
     */
    @JsonProperty(value = "message")
    protected String message;

    /**
     * Method of request that produced the error.
     */
    @JsonProperty(value = "method")
    protected String reqMethod = "Not available";

    @JsonIgnore
    protected HttpStatus httpStatus;

    @JsonProperty("trace_id")
    protected String traceId;

    @JsonProperty(value = "timestamp")
    protected ZonedDateTime timestamp;
    
    public ApiResponse() {
        this.traceId = ThreadContext.get("X-B3-TraceId");
        this.timestamp = TimeZonesConverter.getZonedDateTimeInUTC();
    }
}
