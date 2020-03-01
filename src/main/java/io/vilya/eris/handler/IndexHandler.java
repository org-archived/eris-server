package io.vilya.eris.handler;

import java.util.Arrays;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;

/**
 * 
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-01 14:41
 */
public class IndexHandler implements Handler<RoutingContext> {

    private TemplateEngine templateEngine;

    public IndexHandler(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void handle(RoutingContext context) {
        JsonObject model = new JsonObject();
        model.put("content", content());
        Future<Buffer> view = templateEngine.render(model, "template/index.html");
        context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(view.result());
    }

    private static String content() {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse("" + 
                "```java\r\n" + 
                "@Override\r\n" + 
                "public void handle(RoutingContext context) {\r\n" + 
                "    JsonObject model = new JsonObject();\r\n" + 
                "    model.put(\"content\", content());\r\n" + 
                "    Future<Buffer> view = templateEngine.render(model, \"template/index.html\");\r\n" + 
                "    context.response().putHeader(HttpHeaders.CONTENT_TYPE, \"text/html\").end(view.result());\r\n" + 
                "}\r\n" + 
                "```");
        return renderer.render(document);
    }

}
