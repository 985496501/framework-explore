package com.example.okhttp.interceptor;

/**
 * Application interceptors
 *
 * Don't need to worry about intermediate responses like redirects and retries.
 * Are always invoked once, even if the HTTP response is served from the cache.
 * Observe the application's original intent. Unconcerned with OkHttp-injected headers like If-None-Match.
 * Permitted to short-circuit and not call Chain.proceed().
 * Permitted to retry and make multiple calls to Chain.proceed().
 *
 * Network Interceptors
 * Able to operate on intermediate responses like redirects and retries.
 * Not invoked for cached responses that short-circuit the network.
 * Observe the data just as it will be transmitted over the network.
 * Access to the Connection that carries the request.
 *
 *
 * @author: jinyun
 * @date: 2021/3/12
 */
public class InterceptorTest {

}
