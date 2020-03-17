package io.vilya.eris.handler;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;

/**
 *
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-01 14:41
 */
public class AdminHandler implements Handler<RoutingContext> {

    private final TemplateEngine templateEngine;

    public AdminHandler(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void handle(RoutingContext context) {
        JsonObject model = new JsonObject();
        Future<Buffer> view = templateEngine.render(model, "template/todo.html");
        context.response().putHeader("content-type", "text/html").end(view.result());
    }

}
