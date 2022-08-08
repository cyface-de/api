/*
 * Copyright 2022 Cyface GmbH
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
package de.cyface.api.model;

import io.vertx.core.json.JsonObject;

/**
 * An interface for {@link ClassifiedSegment} based factories.
 *
 * @author Armin Schnabel
 * @version 1.0.1
 * @since 1.0.0
 */
public interface ClassifiedSegmentFactory<T extends ClassifiedSegment> {

    /**
     * Builds a new {@link T} from a database object.
     *
     * @param segment the database object to build the segment from
     * @return The {@code T} built
     */
    T build(final JsonObject segment);
}
