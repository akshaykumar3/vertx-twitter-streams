package demo.twitter;

import io.vertx.core.Vertx;

public class TwitterStreamsVerticle {

    public static void main(String[] args) {
        Vertx.vertx().createHttpServer().requestHandler(req -> {
            req.response().putHeader("Content-type", "text/plain").end(TwitterHelper.get());
        }).listen(8080);
    }
}
