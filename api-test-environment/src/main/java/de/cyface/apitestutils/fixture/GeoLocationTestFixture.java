/*
 * Copyright 2020-2023 Cyface GmbH
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.cyface.model.MeasurementIdentifier;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.mongo.MongoClient;

/**
 * A fixture providing data to use for testing the raw geographical location export.
 * <p>
 * Inserts a test {@link DatabaseConstants#GROUP_MANAGER_ROLE_SUFFIX} user and a test
 * {@link DatabaseConstants#USER_GROUP_ROLE_SUFFIX} user and references the group user as data owner in the created
 * data fixtures.
 * <p>
 * {@link #insertTestData(MongoClient)} returns the user identifier of that data owner.
 *
 * @author Klemens Muthmann
 * @author Armin Schnabel
 * @version 5.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused") // API
public final class GeoLocationTestFixture implements TestFixture {
    /**
     * The name of the test group to export test data of.
     */
    private static final String TEST_GROUP = "testGroup";
    /**
     * The name of the user to add test data for in group-data accessing tests.
     */
    public static final String TEST_GROUP_USER_USERNAME = TEST_GROUP + "1";
    /**
     * The user which is used for authentication in the test.
     */
    public static final String TEST_USER_NAME = "admin";
    /**
     * The identifiers of the measurements to be used during the test.
     */
    private final List<MeasurementIdentifier> testMeasurementIdentifiers;

    /**
     * Creates a new completely initialized fixture for the test.
     *
     * @param testMeasurementIdentifiers The identifiers of the measurements to be used during the test.
     */
    @SuppressWarnings("unused") // API
    public GeoLocationTestFixture(final List<MeasurementIdentifier> testMeasurementIdentifiers) {
        this.testMeasurementIdentifiers = testMeasurementIdentifiers;
    }

    @Override
    public Future<UUID> insertTestData(MongoClient mongoClient) {

        // Insert of test group manager and -user removed after switching to OAuth
        final var userId = UUID.randomUUID();

        final Promise<UUID> promise = Promise.promise();
        final var testDocuments = testMeasurementIdentifiers.stream().map(
                id -> new TestMeasurementDocument(userId, id.getMeasurementIdentifier(), id.getDeviceIdentifier()))
                .collect(Collectors.toList());

        // noinspection rawtypes
        final var futures = testDocuments.stream().map(d -> (Future)d.insert(mongoClient))
                .collect(Collectors.toList());

        final CompositeFuture composition = CompositeFuture.all(futures);
        composition.onSuccess(succeeded -> promise.complete(userId));
        composition.onFailure(promise::fail);

        return promise.future();
    }
}
