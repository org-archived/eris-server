/**
 * 
 */
package io.vilya.eris.router.tool;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;

/**
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-29 21:02
 */
public class BV2AVRouter {

    private BV2AVRouter() {
    }

    public static Router createRouter(Vertx vertx, TemplateEngine templateEngine) {
        // TODO 1、sington
        // TODO 2、java.net.http
        HttpClient httpClient = vertx.createHttpClient();
        
        Router router = Router.router(vertx);
        router.post("/api").handler(context -> {
            System.out.println(context.normalisedPath());
            context.response().putHeader("content-type", "text/html").end("123");
        });
        
        router.get().handler(context -> {
            System.out.println(context.normalisedPath());
            context.response().putHeader("content-type", "text/html").end("213");
        });
        
        return router;
    }
    

}
