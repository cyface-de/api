/*
 * Copyright 2019-2022 Cyface GmbH
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

/**
 * Constants used in the database containing the compressed serialized data received from clients.
 *
 * @author Armin Schnabel
 * @version 1.2.1
 * @since 1.0.0
 */
public class DatabaseConstants {

    /**
     * The prefix for user roles which defines an user with "manager" permissions.
     * <p>
     * The current semantic of the manager role is the following:
     * The users with the role "myGroup_manager" can access data of all users with role
     * "myGroup"+{@code #USER_GROUP_ROLE_PREFIX}
     */
    public static final String GROUP_MANAGER_ROLE_SUFFIX = "_manager";
    /**
     * The prefix for user roles which define a user "group" as there is no such thing in the vertx mongo auth provider.
     * <p>
     * The current semantic of the user role is the following:
     * The user data of all users with the role "myGroup_user" can be accessed by users with role
     * "myGroup"+{@code #GROUP_MANAGER_ROLE_PREFIX}
     */
    public static final String USER_GROUP_ROLE_SUFFIX = "_user";
    /**
     * The database collection name.
     */
    public static final String COLLECTION_USER = "user";
    /**
     * The database collection name.
     */
    public static final String COLLECTION_FILES = "fs.files";
    /**
     * The database collection name.
     */
    public static final String COLLECTION_CHUNKS = "fs.chunks";
    /**
     * The database field name.
     */
    public static final String USER_USERNAME_FIELD = "username";
    /**
     * The database field name used to annotate the owner of data by its user id.
     */
    public static final String USER_ID_FIELD = "userId";
    /**
     * The database field name.
     */
    public static final String METADATA_FIELD = "metaData";
    /**
     * The database field name.
     */
    public static final String METADATA_DEVICE_ID_FIELD = "deviceId";
    /**
     * The database field name used to annotate the device type in the metadata.
     */
    public static final String METADATA_DEVICE_TYPE_FIELD = "deviceType";
    /**
     * The database field name used to annotate the os version in the metadata.
     */
    public static final String METADATA_OS_VERSION_FIELD = "osVersion";
    /**
     * The database field name used to annotate the app version in the metadata.
     */
    public static final String METADATA_APP_VERSION_FIELD = "appVersion";
    /**
     * The database field name used to annotate the length of the measurement in the metadata.
     */
    public static final String METADATA_LENGTH_FIELD = "length";
    /**
     * The database field name used to annotate the version of the metadata format.
     */
    public static final String METADATA_VERSION_FIELD = "version";
    /**
     * The database field name.
     */
    public static final String METADATA_MEASUREMENT_ID_FIELD = "measurementId";
    /**
     * The database field name used to annotate the track bucket of a measurement in the deserialized collection.
     */
    public static final String DESERIALIZED_TRACK_FIELD = "track";
    /**
     * The database field name.
     */
    public static final String ID_FIELD = "_id";
    /**
     * The database field name.
     */
    public static final String CHUNKS_FILES_ID_FIELD = "files_id";
    /**
     * The database field name.
     */
    public static final String CHUNKS_DATA_FIELD = "data";
    /**
     * The database field name.
     */
    public static final String CHUNKS_DATA_BINARY_FIELD = "$binary";
    /**
     * The file extension of Compressed Cyface "Events" binary files.
     */
    public static final String METADATA_FILE_TYPE_CCYF = "ccyf";
}
