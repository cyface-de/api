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
package de.cyface.apitestutils.fixture;

import de.cyface.apitestutils.fixture.user.TestUser;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.mongo.MongoClient;

/**
 * A fixture providing data to use for testing the user activation endpoint.
 *
 * @author Armin Schnabel
 * @version 2.0.1
 * @since 1.0.0
 */
@SuppressWarnings("unused") // API
public final class UserTestFixture implements TestFixture {

    /**
     * The measurements used during the test
     */
    private final TestUser testUser;

    /**
     * Creates a new completely initialized fixture for the test.
     *
     * @param user The user to add
     */
    @SuppressWarnings("unused") // API
    public UserTestFixture(final TestUser user) {
        this.testUser = user;
    }

    @Override
    public Future<String> insertTestData(final MongoClient mongoClient) {
        final Promise<String> promise = Promise.promise();
        final var insert = testUser.insert(mongoClient);
        insert.onSuccess(promise::complete);
        insert.onFailure(promise::fail);
        return promise.future();
    }
}
