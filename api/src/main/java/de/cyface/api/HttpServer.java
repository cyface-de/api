/*
 * Copyright 2020-2022 Cyface GmbH
 *
 * This file is part of the Cyface API Library.
 *
 * The Cyface API Library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Cyface API Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Cyface API Library. If not, see <http://www.gnu.org/licenses/>.
 */
package de.cyface.api;

import java.util.Collections;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

/**
 * A wrapper which starts the {@code HttpServer} for {@code ApiVerticle}s.
 *
 * @author Armin Schnabel
 * @version 1.0.2
 * @since 1.0.0
 */
@SuppressWarnings("unused") // Part of the API
public class HttpServer {

    /**
     * The <code>Logger</code> used for objects of this class. Configure it by changing the settings in
     * <code>src/main/resources/vertx-default-jul-logging.properties</code>.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    /**
     * The port on which the HTTP server should listen
     */
    private final int port;

    /**
     * @param port The port on which the HTTP server should listen
     */
    @SuppressWarnings("unused") // Part of the API
    public HttpServer(final int port) {
        this.port = port;
    }

    /**
     * Starts the HTTP server provided by this application. This server runs the Cyface Collector REST-API.
     *
     * @param vertx The Vertx instance to get the parameters from
     * @param router The router for all the endpoints the HTTP server should serve
     * @param startPromise Informs the caller about the successful or failed start of the server
     */
    @SuppressWarnings("unused") // Part of the API
    public void start(final Vertx vertx, final Router router, final Promise<Void> startPromise) {
        Validate.notNull(router);
        Validate.notNull(startPromise);

        final var options = new HttpServerOptions();
        options.setCompressionSupported(true);
        // Make server respond with one of the sub-protocols sent by our client, in our case: ['Bearer', 'eyToken***'].
        // When the server does not respond with one of both options, the websocket client won't accept the connection.
        // The protocol name "Bearer" is made up, but injecting the auth token via protocol is a common workaround
        // for the issue that the Javascript Websocket class does not allow to add an `Authorization` header [RFR-165].
        options.setWebSocketSubProtocols(Collections.singletonList("Bearer"));
        vertx.createHttpServer(options)
                .requestHandler(router)
                .listen(port, serverStartup -> completeStartup(serverStartup, startPromise));
    }

    /**
     * Finishes the <code>CollectorApiVerticle</code> startup process and informs all interested parties about whether
     * it has been successful or not.
     *
     * @param serverStartup The result of the server startup as provided by <code>Vertx</code>
     * @param promise A promise to call to inform all waiting parties about success or failure of the startup process
     */
    private void completeStartup(final AsyncResult<io.vertx.core.http.HttpServer> serverStartup,
            final Promise<Void> promise) {
        if (serverStartup.succeeded()) {
            promise.complete();
            LOGGER.info("Successfully started API!");
        } else {
            promise.fail(serverStartup.cause());
            LOGGER.info("Starting API failed!");
        }
    }
}
