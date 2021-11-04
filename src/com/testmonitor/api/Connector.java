package com.testmonitor.api;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.FileBody;

import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.json.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


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

    /**
     * @return The TestMonitor API base url.
     */
    protected String baseUrl()
    {
        return "https://" + this.domain + "/api/v1/";
    }

    /**
     * Perform a GET request.
     *
     * @param uri A relative path
     *
     * @return The HTTP response as a JSONObject.
     */
    public JSONObject get(String uri)
    {
        final HttpGet httpget = new HttpGet(this.baseUrl + uri.replace(" ", "%20"));

        return this.request(httpget);
    }

    /**
     * Perform a GET request with GET parameters.
     *
     * @param uri A relative path
     * @param params Key/Value
     *
     * @return The HTTP response as a JSONObject.
     */
    public JSONObject get(String uri, List<NameValuePair> params)
    {
        URIBuilder uriBuilder = null;

        try {
            uriBuilder = new URIBuilder(this.baseUrl + uri);
            uriBuilder.addParameters(params);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        final HttpGet httpget = new HttpGet(uriBuilder.toString());

        httpget.setEntity(new UrlEncodedFormEntity(params));

        return this.request(httpget);
    }

    /**
     * Perform a POST request.
     *
     * @param uri A relative path
     * @param params The arguments to post
     *
     * @return The HTTP response as a JSONObject.
     */
    public JSONObject post(String uri, List<NameValuePair> params)
    {
        final HttpPost httppost = new HttpPost(this.baseUrl + uri);

        httppost.setEntity(new UrlEncodedFormEntity(params));

        return this.request(httppost);
    }

    /**
     * Perform a PUT request.
     *
     * @param uri A relative path
     * @param params The arguments to put
     *
     * @return The HTTP response as a JSONObject.
     */
    public JSONObject put(String uri, List<NameValuePair> params)
    {
        final HttpPut httpput = new HttpPut(this.baseUrl + uri);

        httpput.setEntity(new UrlEncodedFormEntity(params));

        return this.request(httpput);
    }

    /**
     * Send am attachment.
     *
     * @param uri A relative path
     * @param file the file you want to send as an attachment
     *
     * @return The HTTP response as a JSONObject.
     */
    public JSONObject postAttachment(String uri, File file)
    {
        HttpPost post = new HttpPost(this.baseUrl + uri);
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.EXTENDED);
        builder.addPart("file", fileBody);

        HttpEntity entity = builder.build();

        post.setEntity(entity);

        return this.request(post);
    }

    /**
     * Perform a request on teh TestMonitor API
     *
     * @param httpUriRequestBase
     *
     * @return HTTP response converted to JSON format
     */
    public JSONObject request(HttpUriRequestBase httpUriRequestBase) {
        httpUriRequestBase.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.token);
        httpUriRequestBase.setHeader(HttpHeaders.ACCEPT, "application/json");

        final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<>() {
            @Override
            public String handleResponse(
                    final ClassicHttpResponse response) throws IOException, ParseException {
                final int status = response.getCode();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    final HttpEntity entity = response.getEntity();
                    try {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    throw new ClientProtocolException(
                        "Unexpected response status: " + status + "\n" + EntityUtils.toString(response.getEntity())
                    );
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
