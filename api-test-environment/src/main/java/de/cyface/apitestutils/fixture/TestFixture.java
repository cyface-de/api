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
package de.cyface.apitestutils.fixture;

import de.cyface.apitestutils.TestEnvironment;
import io.vertx.core.Future;
import io.vertx.ext.mongo.MongoClient;

/**
 * A provider for test fixture data. Such a provider is required by {@link TestEnvironment} instances.
 *
 * @author Klemens Muthmann
 * @version 2.0.1
 * @since 1.0.0
 * @see TestEnvironment
 */
public interface TestFixture {
    /**
     * Insert some test data into a test Mongo database via the provided <code>mongoClient</code>.
     *
     * @param mongoClient The client to access the Mongo database hosting the test data
     * @return A {@code Future} which is resolved after inserting the data has completed
     */
    Future<String> insertTestData(final MongoClient mongoClient);
}
