package io.vilya.eris;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import io.vilya.eris.handler.DefaultHandler;
import io.vilya.eris.handler.IndexHandler;

/**
 * 
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-01 14:42
 */
public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Future<HttpServer> future = vertx.createHttpServer().requestHandler(this::handle).listen(8888);
        future.onSuccess(httpServer -> logger.info("Started")).onFailure(cause -> logger.error("Failed", cause));
    }

    private void handle(HttpServerRequest request) {
        TemplateEngine templateEngine = ThymeleafTemplateEngine.create(vertx);

        Router router = Router.router(vertx);

        router.routeWithRegex(HttpMethod.GET, "/admin/.*").handler(routingContext -> {
            JsonObject model = new JsonObject();
            Future<Buffer> view = templateEngine.render(model, "template/todo.html");
            routingContext.response().putHeader("content-type", "text/html").end(view.result());
        });

        router.routeWithRegex(HttpMethod.GET, "/t/\\d+\\.html").handler(routingContext -> {
            JsonObject model = new JsonObject();
            Future<Buffer> view = templateEngine.render(model, "template/todo.html");
            routingContext.response().putHeader("content-type", "text/html").end(view.result());
        });

        router.routeWithRegex(HttpMethod.GET, "/u/\\d+\\.html").handler(routingContext -> {
            JsonObject model = new JsonObject();
            Future<Buffer> view = templateEngine.render(model, "template/todo.html");
            routingContext.response().putHeader("content-type", "text/html").end(view.result());
        });

        router.route(HttpMethod.GET, "/").handler(new IndexHandler(templateEngine));
        router.routeWithRegex(HttpMethod.GET, ".*").handler(new DefaultHandler());

        router.handle(request);
    }
}
