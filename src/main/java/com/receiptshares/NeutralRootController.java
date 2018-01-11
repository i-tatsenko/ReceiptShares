package com.receiptshares;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
//@Configuration
public class NeutralRootController {
    private static final Logger LOG = LoggerFactory.getLogger(NeutralRootController.class);

    @Value("classpath:/static/html/index.html")
    private Resource indexPageContent;

    @RequestMapping("/")
    public Mono<ResponseEntity> index() {
        return Mono.just(ResponseEntity.ok().body(indexPageContent));
//        return "/html/index.html";
    }

    @RequestMapping("/{path:(?!(?:v1|html|css|fonts|images|js)).*}/**")
    public Mono<ResponseEntity> forward() {
        return Mono.just(ResponseEntity.ok().body(indexPageContent));
//        return "/html/index.html";
    }

//    @Bean
    public RouterFunction router() throws URISyntaxException {
        return route(GET("/"), r -> ok().syncBody(indexPageContent))
                .and(route(GET("/{path:(?!(?:v1|html|css|fonts|images|js)).*}/**"), r -> ok().syncBody(indexPageContent)))
//                .and(resources("/js/**", new ClassPathResource("/static/js/")))
                ;
    }

    private HandlerFunction<ServerResponse> redirectToIndex() throws URISyntaxException {
        URI uri = new URI("/html/index.html");
        return request -> ServerResponse.permanentRedirect(uri).build();
    }
}
