package io.vilya.eris.router.tool;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;

/**
 *
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-01 14:41
 */
public class ToolRouter {

    private ToolRouter() {
    }

    public static Router createRouter(Vertx vertx, TemplateEngine templateEngine) {
        Router router = Router.router(vertx);

        router.mountSubRouter("/bv2av", BV2AVRouter.createRouter(vertx, templateEngine));

        return router;
    }

}
