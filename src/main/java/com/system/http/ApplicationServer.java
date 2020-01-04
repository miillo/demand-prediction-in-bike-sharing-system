package com.system.http;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.system.settings.AppSettings;

import java.util.concurrent.CompletionStage;

public class ApplicationServer extends AllDirectives {
    private LoggingAdapter log;
    private ActorRef processingDataActor;
    private CompletionStage<ServerBinding> serverBinding;

    public ApplicationServer() {}

    public ApplicationServer(ActorSystem actorSystem, ActorRef processingDataActor) {
        this.log = Logging.getLogger(actorSystem, this);
        this.processingDataActor = processingDataActor;
        this.serverBinding = runAppServer(actorSystem);
        log.info("Application server started | " + AppSettings.httpDomain + ":" + AppSettings.httpPort);
    }

    /**
     * Creates application routes
     *
     * @return application routes instance
     */
    private Route createRoute() {
        return concat(
                path("hello", () ->
                        get(() ->
                                complete("hello"))));
    }

    /**
     * Runs application server
     *
     * @param actorSystem actor system
     * @return server binding
     */
    private CompletionStage<ServerBinding> runAppServer(ActorSystem actorSystem) {
        Http http = Http.get(actorSystem);
        ActorMaterializer actorMaterializer = ActorMaterializer.create(actorSystem);
        ApplicationServer app = new ApplicationServer();
        Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(actorSystem, actorMaterializer);
        return http.bindAndHandle(routeFlow, ConnectHttp.toHost(AppSettings.httpDomain, AppSettings.httpPort), actorMaterializer);
    }

    /**
     * Stops application server
     */
    private void stopAppServer() {
        serverBinding.thenCompose(ServerBinding::unbind);
    }
}
