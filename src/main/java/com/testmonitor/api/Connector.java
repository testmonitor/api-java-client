package com.testmonitor.api;

import com.testmonitor.exceptions.NotFoundException;
import com.testmonitor.exceptions.ServerException;
import com.testmonitor.exceptions.UnprocessableEntityException;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Connector {

    private final String token;

    private final String domain;

    private final CloseableHttpClient httpClient;

    /**
     * @param domain The TestMonitor domain
     * @param token The auth token
     */
    public Connector(String domain, String token) {
        this.token = token;
        this.domain = domain;

        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(5000, TimeUnit.MILLISECONDS)
                .setConnectTimeout(5000, TimeUnit.MILLISECONDS)
                .setConnectionRequestTimeout(5000, TimeUnit.MILLISECONDS)
                .setCookieSpec(StandardCookieSpec.IGNORE)
                .build();

        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent("TestMonitorJavaClient/" + Client.VERSION + " (+https://www.testmonitor.com/)")
                .build();
    }

    /**
     * @return The TestMonitor API base url.
     */
    protected String baseUrl()
    {
        return "https://" + this.domain + "/api/v1/";
    }

    /**
     * @param path The path to concatenate
     *
     * @return The TestMonitor API base url.
     */
    protected String baseUrl(String path)
    {
        return this.baseUrl() + path;
    }

    /**
     * Perform a GET request.
     *
     * @param uri A relative path
     *
     * @return The HTTP response as a JSONObject.
     */
    public JSONObject get(String uri) throws IOException {
        final HttpGet httpget = new HttpGet(this.baseUrl(uri));

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
    public JSONObject get(String uri, List<NameValuePair> params) throws IOException, URISyntaxException {
        URIBuilder uriBuilder = null;

        uriBuilder = new URIBuilder(this.baseUrl(uri));
        uriBuilder.addParameters(params);

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
    public JSONObject post(String uri, List<NameValuePair> params) throws IOException {
        final HttpPost httppost = new HttpPost(this.baseUrl(uri));

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
    public JSONObject put(String uri, List<NameValuePair> params) throws IOException {
        final HttpPut httpput = new HttpPut(this.baseUrl(uri));

        httpput.setEntity(new UrlEncodedFormEntity(params));

        return this.request(httpput);
    }

    /**
     * Send a post request using multipart.
     *
     * @param uri A relative path
     * @param params The arguments to post
     * @param files The files to post
     *
     * @return The HTTP response as a JSONObject.
     */
    public JSONObject multiPartPost(String uri, List<NameValuePair> params, HashMap<String, File> files) throws IOException {
        HttpPost httppost = new HttpPost(this.baseUrl(uri));

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.EXTENDED);

        // Handle plain post parameters
        params.forEach((pair -> builder.addTextBody(pair.getName(), pair.getValue())));

        // Handle file attachments
        files.forEach((name, file) -> builder.addPart("attachments[]", new FileBody(file, ContentType.DEFAULT_BINARY)));

        HttpEntity entity = builder.build();

        httppost.setEntity(entity);

        return this.request(httppost);
    }

    /**
     * Send an attachment.
     *
     * @param uri A relative path
     * @param name Field name
     * @param file The file attachment
     *
     * @return The HTTP response as a JSONObject.
     */
    public JSONObject postFile(String uri, String name, File file) throws IOException {
        HttpPost post = new HttpPost(this.baseUrl(uri));

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.EXTENDED);
        builder.addPart(name, new FileBody(file, ContentType.DEFAULT_BINARY));

        HttpEntity entity = builder.build();

        post.setEntity(entity);

        return this.request(post);
    }

    /**
     * Perform a request on teh TestMonitor API
     *
     * @param httpUriRequestBase The HTTP request
     *
     * @return HTTP response converted to JSON format
     */
    public JSONObject request(HttpUriRequestBase httpUriRequestBase) throws IOException {
        httpUriRequestBase.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.token);
        httpUriRequestBase.setHeader(HttpHeaders.ACCEPT, "application/json");

        final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {
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
                    if (status == 500) {
                        throw new ServerException(status, EntityUtils.toString(response.getEntity()));
                    } else if (status == 422) {
                        throw new UnprocessableEntityException(status, EntityUtils.toString(response.getEntity()));
                    } else if (status == 404) {
                        throw new NotFoundException(status, EntityUtils.toString(response.getEntity()));
                    } else {
                        throw new ClientProtocolException(
                                "Unexpected response status: " + status + "\n" + EntityUtils.toString(response.getEntity())
                        );
                    }
                }
            }
        };

        final String responseBody;

        responseBody = this.httpClient.execute(httpUriRequestBase, responseHandler);

        return new JSONObject(responseBody);
    }
}
