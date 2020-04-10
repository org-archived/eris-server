package io.vilya.eris;

import io.vertx.core.Vertx;
import io.vertx.core.file.impl.FileResolver;
import io.vertx.ext.web.common.WebEnvironment;

/**
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-01 14:42
 */
public class Application {
    public static void main(String[] args) {
        System.setProperty(WebEnvironment.SYSTEM_PROPERTY_NAME, "dev");
        System.setProperty(FileResolver.DISABLE_FILE_CACHING_PROP_NAME, "true");
        
        
        Vertx vertx = Vertx.factory.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
