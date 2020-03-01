package io.vilya.eris;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpStatusClass;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
    
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Future<HttpServer> future = vertx.createHttpServer()
                .requestHandler(this::handle)
                .listen(8888);
        future.onSuccess(httpServer -> logger.info("Started")).onFailure(cause -> logger.error("Failed", cause));
    }
    
    private void handle(HttpServerRequest request) {
        TemplateEngine templateEngine = ThymeleafTemplateEngine.create(vertx);
        
        Router router = Router.router(vertx);
        
        Route route = router.routeWithRegex(HttpMethod.GET, "/admin/.*");
        route.handler(routingContext -> {
            JsonObject model = new JsonObject();
            Future<Buffer> view = templateEngine.render(model, "template/index.html");
            routingContext.response().putHeader("content-type", "text/html").end(view.result());
        });
        
        router.routeWithRegex(HttpMethod.GET, ".*").handler(routingContext -> {
            routingContext.response().putHeader("content-type", "text/html").setStatusCode(404).end("404");
        });
        
        router.handle(request);
    }
}
