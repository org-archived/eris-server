/**
 * 
 */
package io.vilya.eris.router.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vilya.eris.domain.Result;

/**
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-29 21:02
 */
public class BV2AVRouter {

    private static final Logger log = LoggerFactory.getLogger(BV2AVRouter.class);

    private static final String BILIBILI_API = "https://api.bilibili.com/x/web-interface/view?bvid=BV1qK41177vS";

    private BV2AVRouter() {
    }

    public static Router createRouter(Vertx vertx, TemplateEngine templateEngine) {
        // TODO 1、sington
        // TODO 2、java.net.http
        HttpClient httpClient = vertx.createHttpClient();

        Router router = Router.router(vertx);
        router.post("/api").handler(context -> {
            MultiMap queryParams = context.queryParams();
            String bv = queryParams.get("bv");
            String av = queryParams.get("av");

            if (Strings.isNullOrEmpty(bv) && Strings.isNullOrEmpty(av)) {
                context.json(Result.failed("缺少参数"));
            }

            String url;
            if (!Strings.isNullOrEmpty(bv)) {
                url = BILIBILI_API + "?bvid=" + bv;
            } else {
                url = BILIBILI_API + "?aid=" + av;
            }

            httpClient.get(url).onSuccess(response -> {
                if (response.statusCode() != 200) {
                    log.error("statusCode={}", response.statusCode());
                    context.json(Result.failed("处理请求失败"));
                } else {
                    JsonObject body = Json.decodeValue(response.body().result(), JsonObject.class);
                    JsonObject data = body.getJsonObject("data");
                    JsonObject ret = new JsonObject();
                    ret.put("av", data.getLong("aid"));
                    ret.put("bv", data.getString("bvid"));
                    context.json(Result.succeeded(ret));
                }
            }).onFailure(exception -> context.json(Result.failed(exception.getMessage())));
            context.end();
        });

        router.get().handler(context -> {
            System.out.println(context.normalisedPath());
            context.response().putHeader("content-type", "text/html").end("213");
        });

        router.errorHandler(500, context -> log.error("", context.failure()));

        return router;
    }

}
