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
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
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

    private static final String BILIBILI_API = "https://api.bilibili.com/x/web-interface/view";

    private BV2AVRouter() {
    }

    public static Router createRouter(Vertx vertx, TemplateEngine templateEngine) {
        HttpClientOptions httpClientOptions = new HttpClientOptions();
        HttpClient httpClient = vertx.createHttpClient(httpClientOptions);

        Router router = Router.router(vertx);
        router.post("/api").handler(context -> {
            JsonObject requestBody = context.getBodyAsJson();
            if (requestBody == null) {
                context.json(Result.failed("缺少参数"));
            }
            
            String bv = requestBody.getString("bv");
            String av = requestBody.getString("av");

            if (Strings.isNullOrEmpty(bv) && Strings.isNullOrEmpty(av)) {
                context.json(Result.failed("缺少参数"));
            }

            String url;
            if (!Strings.isNullOrEmpty(bv)) {
                url = BILIBILI_API + "?bvid=" + bv;
            } else {
                url = BILIBILI_API + "?aid=" + av;
            }

            log.info("url={}", url);

            HttpClientRequest request = httpClient.getAbs(url);
            request.onSuccess(response -> {
                if (response.statusCode() != 200) {
                    log.error("statusCode={}", response.statusCode());
                    context.json(Result.failed("处理请求失败"));
                } else {
                    response.body().onSuccess(buffer -> {
                        JsonObject body = (JsonObject) Json.decodeValue(buffer);
                        JsonObject data = body.getJsonObject("data");
                        JsonObject ret = new JsonObject();
                        ret.put("av", data.getLong("aid"));
                        ret.put("bv", data.getString("bvid"));
                        context.json(Result.succeeded(ret));
                    });
                }
            }).onFailure(exception -> {
                log.error("", exception);
                context.json(Result.failed(exception.getMessage()));
            });
            request.end();
        });

        router.get().handler(context -> {
            templateEngine.render(new JsonObject(), "h/bv2av.html").onSuccess(context::end);
        });

        router.errorHandler(500, context -> log.error("", context.failure()));

        return router;
    }

}
