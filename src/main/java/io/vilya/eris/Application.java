package io.vilya.eris;

import io.vertx.core.Vertx;

/**
 * 
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-01 14:42
 */
public class Application {

	public static void main(String[] args) {
		Vertx vertx = Vertx.factory.vertx();
		vertx.deployVerticle(new MainVerticle());
	}
	
}
