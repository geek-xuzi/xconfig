package com.xuen.xconfig.util;

import com.ning.http.client.Response;
import java.io.IOException;

/**
 * @author zheng.xu
 * @since 2017-05-17
 */
@FunctionalInterface
public  interface HttpRun {

    void handler(Response response) throws IOException;

}
