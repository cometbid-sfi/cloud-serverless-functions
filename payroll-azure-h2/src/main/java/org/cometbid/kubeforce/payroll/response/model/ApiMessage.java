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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author samueladebowale
 */
@Data
@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = false)
public class ApiMessage extends ApiResponse implements Serializable {

    @JsonIgnore
    @JsonProperty("type")
    private ResponseType responseTyp;

    @JsonIgnore
    @JsonProperty("responses")
    private List<Response> responses;

    @Builder
    public ApiMessage(HttpStatus httpStatus, String reqMethod, String path, String message, String detailMessage) {
        this();
        this.httpStatus = httpStatus;
        this.reqMethod = reqMethod;
        this.path = path;
        this.message = message;
        this.responses = new ArrayList<>();
    }

    @JsonCreator
    public ApiMessage(HttpStatus httpStatus, String reqMethod, String path, String message) {
        this();
        this.httpStatus = httpStatus;
        this.reqMethod = reqMethod;
        this.path = path;
        this.message = message;
        this.responses = new ArrayList<>();
    }

    public ApiMessage() {
        super();
        this.responseTyp = null;
        this.responses = new ArrayList<>();
    }

    public int getStatus() {
        return httpStatus.value();
    }

    public String getReason() {
        return httpStatus.getReasonPhrase();
    }

    public void add(String path, String code, String message) {
        this.responses.add(new Response(path, code, message));
    }

    @ToString
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = false)
    public class Response implements Serializable {

        private String path;
        private String code;
        private String message;

        @JsonCreator
        Response(String path, String code, String message) {
            this.path = path;
            this.code = code;
            this.message = message;
        }

    }
}
