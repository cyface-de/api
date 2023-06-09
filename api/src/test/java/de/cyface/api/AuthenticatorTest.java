/*
 * Copyright 2022-2023 Cyface GmbH
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.mongo.MongoAuthentication;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;

/**
 * This class checks the inner workings of the {@link Authenticator}.
 *
 * @author Armin Schnabel
 * @author Klemens Muthmann
 * @version 1.1.1
 * @since 1.0.0
 */
@SuppressWarnings({"SpellCheckingInspection"})
public class AuthenticatorTest {

    /**
     * Only self-registered accounts need to be activated and, thus, a missing "activated" field counts as activated.
     */
    @Test
    void testActivated() {
        // Arrange
        final var registeredPrincipal = new JsonObject().put("username", "guest").put("activated", false);
        final var activatedPrincipal = new JsonObject().put("username", "guest").put("activated", true);
        final var createdPrincipal = new JsonObject().put("username", "guest");

        // Act
        final var registeredResult = Authenticator.activated(registeredPrincipal);
        final var activatedResult = Authenticator.activated(activatedPrincipal);
        final var createdResult = Authenticator.activated(createdPrincipal);

        // Assert
        assertFalse(registeredResult, "Check activation");
        assertTrue(activatedResult, "Check activation");
        assertTrue(createdResult, "Check activation");
    }

    /**
     * Checks that usernames are handled ignoring letter casing.
     */
    @Test
    void testUsernameIsCaseInsensitive() {
        // Arrange
        final var mockAuthentication = mock(MongoAuthentication.class);
        final var mockAuthProvider = mock(JWTAuth.class);
        final var issuer = "de.cyface";
        final var audience = "de.cyface.api";
        final var tokenValidationTime = 1000;
        final var mockContext = mock(RoutingContext.class);
        final var mockResponse = mock(HttpServerResponse.class);
        final var mockBody = mock(RequestBody.class);
        final var mockAuthenticationResult = mock(Future.class);
        final var testCredentials = new JsonObject().put("username", "Username").put("password", "password");
        final var expectedCredentials = new JsonObject().put("username", "username").put("password", "password");

        when(mockContext.response()).thenReturn(mockResponse);
        when(mockContext.body()).thenReturn(mockBody);
        when(mockBody.asJsonObject()).thenReturn(testCredentials);
        when(mockAuthentication.authenticate(any(JsonObject.class))).thenReturn(mockAuthenticationResult);

        final var oocut = new Authenticator(mockAuthentication, mockAuthProvider, issuer, audience,
                tokenValidationTime);

        // Act
        oocut.handle(mockContext);

        // Assert
        verify(mockAuthentication).authenticate(eq(expectedCredentials));
    }
}
