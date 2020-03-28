package io.vilya.eris.handler.tool;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.net.HttpHeaders;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.MIMEHeader;
import io.vertx.ext.web.ParsedHeaderValues;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.ParsableMIMEValue;

/**
 * 
 * @author cafedada <cafedada@vilya.io>
 * @since 2020-03-28 22:08
 */
public class IPHandler implements Handler<RoutingContext> {

    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings();

    private static final VisitorWriter DEFAULT_WRITER = new DefaultVisitorWriter();

    private static final Map<MIMEHeader, VisitorWriter> PRIMARY_SUPPORTED_ACCEPT = Map.of(
            new ParsableMIMEValue("application/json"), new JsonVisitorWriter(),
            new ParsableMIMEValue("application/xml"), new XmlVisitorWriter(),
            new ParsableMIMEValue("text/html"), DEFAULT_WRITER);

    @Override
    public void handle(RoutingContext context) {
        ParsedHeaderValues headers = context.parsedHeaders();
        MIMEHeader accept = headers.findBestUserAcceptedIn(headers.accept(), PRIMARY_SUPPORTED_ACCEPT.keySet());
        
        Visitor visitor = new Visitor(getVisitorIp(context.request()));

        VisitorWriter writer = Optional.<VisitorWriter>ofNullable(getVisitorWriter(accept)).orElse(DEFAULT_WRITER);
        String body = writer.createBody(visitor);

        context.response().putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length()));
        context.response().putHeader(HttpHeaders.CONTENT_TYPE, accept.value() + "; charset=utf-8");

        context.end(body);
    }

    private static String getVisitorIp(HttpServerRequest request) {
        String xff = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        if (!Strings.isNullOrEmpty(xff)) {
            return getVisitorIpFromXFF(xff);
        }
        return request.remoteAddress().host();
    }

    private static String getVisitorIpFromXFF(String xff) {
        return Iterables.getFirst(COMMA_SPLITTER.split(xff), xff);
    }

    private static VisitorWriter getVisitorWriter(MIMEHeader accept) {
        return PRIMARY_SUPPORTED_ACCEPT.get(accept);
    }

    private static class Visitor {

        private String ip;

        public Visitor(String ip) {
            super();
            this.ip = ip;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

    }

    private interface VisitorWriter {

        String createBody(Visitor visitor);

    }

    private static class JsonVisitorWriter implements VisitorWriter {

        private static final ObjectMapper serializer = new ObjectMapper(new JsonFactory());

        @Override
        public String createBody(Visitor visitor) {
            try {
                return serializer.writeValueAsString(visitor);
            } catch (JsonProcessingException e) {
                throw new WriteFailedException(e);
            }
        }

    }

    private static class XmlVisitorWriter implements VisitorWriter {

        private static final ObjectMapper serializer = new XmlMapper();

        @Override
        public String createBody(Visitor visitor) {
            try {
                return serializer.writeValueAsString(visitor);
            } catch (JsonProcessingException e) {
                throw new WriteFailedException(e);
            }
        }

    }

    private static class DefaultVisitorWriter implements VisitorWriter {

        @Override
        public String createBody(Visitor visitor) {
            return visitor.getIp();
        }

    }

    private static class WriteFailedException extends RuntimeException {

        private static final long serialVersionUID = -6976294003829906857L;

        public WriteFailedException(Throwable cause) {
            super(cause);
        }

    }

}
