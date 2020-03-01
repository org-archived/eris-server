package io.vilya.eris;

import io.vertx.core.Vertx;

public class Application {

	public static void main(String[] args) {
		Vertx vertx = Vertx.factory.vertx();
		vertx.deployVerticle(new MainVerticle());
	}
	
}
