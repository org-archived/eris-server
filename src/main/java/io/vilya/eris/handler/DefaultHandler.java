/**
 * 
 */
package io.vilya.eris.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-01 14:45
 */
public class DefaultHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {
        context.response().putHeader("content-type", "text/html").setStatusCode(404).end("Not Found.");
    }

}
