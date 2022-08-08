/*
 * Copyright 2021-2022 Cyface GmbH
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.cyface.model.TrackBucket;
import org.apache.commons.lang3.Validate;

import de.cyface.model.MeasurementIdentifier;
import de.cyface.model.MetaData;
import de.cyface.model.Modality;
import de.cyface.model.Point3DImpl;
import de.cyface.model.RawRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;

// TODO: Probably remove this, as it is not necessary for data collection
/**
 * This class defines the interface to allow multiple {@link MeasurementRetrievalStrategy}s such as
 * {@link MeasurementRetrievalWithSensorData} or {@link MeasurementRetrievalWithoutSensorData}.
 *
 * @author Armin Schnabel
 * @version 1.0.1
 * @since 1.0.0
 */
public interface MeasurementRetrievalStrategy {

    /**
     * Returns the {@code FindOptions} which define which data to load from the database, e.g. all but sensor data.
     *
     * @return the {@code FindOptions} to be used for database access
     */
    FindOptions findOptions();

    /**
     * Returns the {@code Track} list as POJO.
     *
     * @param document the {@code Document} containing the measurement, {@link MetaData#getVersion()} 1.0.0.
     * @return the track list POJO
     * @throws ParseException if the track bucket date cannot be parsed
     */
    TrackBucket trackBucket(final JsonObject document) throws ParseException;

    /**
     * Returns the {@code MetaData} as POJO.
     *
     * @param document the {@code Document} containing the measurement, {@link MetaData#getVersion()} 1.0.0.
     * @return the metadata POJO
     */
    default MetaData metaData(final JsonObject document) {
        final var metaData = document.getJsonObject("metaData");
        final var version = metaData.getString("version");
        Validate.isTrue(version.matches(MetaData.SUPPORTED_VERSIONS),
                "Encountered data in an unsupported deserialized format: %s", version);

        final var identifier = new MeasurementIdentifier(metaData.getString("deviceId"),
                metaData.getLong("measurementId"));
        final var deviceType = metaData.getString("deviceType");
        final var osVersion = metaData.getString("osVersion");
        final var appVersion = metaData.getString("appVersion");
        final var length = metaData.getDouble("length");
        final var userId = metaData.getJsonObject("userId").getString("$oid");
        return new MetaData(identifier, deviceType, osVersion, appVersion, length, userId, version);
    }

    /**
     * Returns the {@code GeoLocationRecord} list as POJO.
     *
     * @param documents the {@code Document} list containing the {@code GeoLocation}s in {@link MetaData#getVersion()}
     *            1.0.0.
     * @param identifier the identifier of the measurement of this track
     * @return the record list POJO
     */
    default List<RawRecord> geoLocations(final JsonArray documents, final MeasurementIdentifier identifier) {
        final var records = new ArrayList<RawRecord>();
        for (int i = 0; i < documents.size(); i++) {
            final var doc = documents.getJsonObject(i);
            final var timestamp = doc.getLong("timestamp");
            final var geometry = doc.getJsonObject("geometry");
            Validate.isTrue(geometry.getString("type").equals("Point"));
            final var coordinates = geometry.getJsonArray("coordinates");
            final var latitude = coordinates.getDouble(1);
            final var longitude = coordinates.getDouble(0);
            final var elevation = doc.getDouble("elevation");
            final var speed = doc.getDouble("speed");
            final var accuracy = doc.getDouble("accuracy");
            final var modality = Modality.valueOf(doc.getString("modality"));
            Validate.notNull(modality, "Unable to identify modality type: " + doc.getString("modality"));
            final var record = new RawRecord(identifier, timestamp, latitude, longitude, elevation, accuracy, speed,
                    modality);
            records.add(record);
        }
        return records;
    }

    /**
     * Returns the {@code Point3D} list as POJO.
     *
     * @param documents the {@code Document} list containing the {@code Point3D}s in {@link MetaData#getVersion()}
     *            1.0.0.
     * @return the point list POJO
     */
    default List<Point3DImpl> point3D(final JsonArray documents) {
        final var point3DS = new ArrayList<Point3DImpl>();
        for (int i = 0; i < documents.size(); i++) {
            final var doc = documents.getJsonObject(i);
            final var timestamp = doc.getLong("timestamp");
            // MongoDB stores all numbers in the same data type
            final var x = doc.getDouble("x").floatValue();
            final var y = doc.getDouble("y").floatValue();
            final var z = doc.getDouble("z").floatValue();
            final var point3D = new Point3DImpl(x, y, z, timestamp);
            point3DS.add(point3D);
        }
        return point3DS;
    }
}