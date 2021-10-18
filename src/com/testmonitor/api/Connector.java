package com.testmonitor.api;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.json.*;

import java.io.IOException;
import java.net.URISyntaxException;


public class Connector {

    private final String token;

    private final String domain;

    private final String baseUrl;

    private final CloseableHttpClient httpClient;

    public Connector(String domain, String token) {
        this.token = token;
        this.domain = domain;

        this.baseUrl = this.baseUrl();

        this.httpClient = HttpClients.createDefault();
    }

    protected String baseUrl()
    {
        return "https://" + this.domain + "/api/v1/";
    }

    public JSONObject get(String uri)
    {
        final HttpGet httpget = new HttpGet(this.baseUrl + uri);

        httpget.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.token);

        return this.request(httpget);
    }

    public void post(String uri)
    {
        final HttpPost httppost = new HttpPost(this.baseUrl + uri);

        System.out.print(this.request(httppost));
    }

    public JSONObject request(HttpUriRequestBase httpUriRequestBase) {
        final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {

            @Override
            public String handleResponse(
                    final ClassicHttpResponse response) throws IOException {
                final int status = response.getCode();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    final HttpEntity entity = response.getEntity();
                    try {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };


        try {
            final String responseBody;

            responseBody = this.httpClient.execute(httpUriRequestBase, responseHandler);

            return new JSONObject(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }
}
