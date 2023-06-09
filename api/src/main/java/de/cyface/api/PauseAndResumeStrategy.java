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

import io.vertx.core.http.HttpServerRequest;

/**
 * The interface for pause and resume strategies to be used which wraps async calls, e.g. in
 * {@code AuthorizationHandler}s in APIs which use this library (e.g. collector, incentives).
 * <p>
 * Use {@link PauseAndResumeBeforeBodyParsing} when the `BodyHandler` is not executed before that handler [DAT-749] or
 * {@link PauseAndResumeAfterBodyParsing} otherwise.
 *
 * @author Armin Schnabel
 * @version 1.0.2
 * @since 1.0.0
 */
public interface PauseAndResumeStrategy {

    /**
     * Pauses the request parsing if necessary while, waiting for an async all.
     *
     * @param request The request to be paused
     */
    void pause(final HttpServerRequest request);

    /**
     * Resumes the request parsing if necessary, after an async call was resolved.
     *
     * @param request The request to be resumed
     */
    void resume(final HttpServerRequest request);
}
