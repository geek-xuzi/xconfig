package com.xuen.xconfig.util;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Request;
import com.ning.http.client.Response;
import com.sun.corba.se.impl.corba.RequestImpl;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
public class HttpUtil {

    private static final AsyncHttpClient HttpClient = new AsyncHttpClient();


    public static void asyncGet(String url) {
        HttpClient.prepareGet(url)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {
                        System.out.println(response.getResponseBody());
                        return response;
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        // Something wrong happened.
                    }
                });
    }

    public static void main(String[] args) {
        HttpUtil.asyncGet("http://www.baidu.com");
    }

}
