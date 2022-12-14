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

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

import io.vertx.ext.auth.HashingStrategy;

/**
 * Used to properly hash passwords for storage to the database.
 * 
 * @author Klemens Muthmann
 * @version 1.0.1
 * @since 1.0.0
 */
public class Hasher {

    /**
     * The Vertx <code>HashingStrategy</code> used for hashing.
     */
    private final HashingStrategy hashingStrategy;
    /**
     * A salt used to obfuscate the hashed password, making it harder to decrypt passwords if the database is
     * compromised.
     */
    private final byte[] salt;

    /**
     * Creates a new completely initialized object of this class.
     *
     * @param hashingStrategy The Vertx <code>HashingStrategy</code> used for hashing
     * @param salt A salt used to obfuscate the hashed password, making it harder to decrypt passwords if the database
     *            is compromised
     */
    public Hasher(final HashingStrategy hashingStrategy, final byte[] salt) {
        Objects.requireNonNull(hashingStrategy);
        Objects.requireNonNull(salt);

        this.hashingStrategy = hashingStrategy;
        this.salt = Arrays.copyOf(salt, salt.length);
    }

    /**
     * Hashes the provided password, according to this strategy instance.
     *
     * @param password The clear text password to hash
     * @return The hashed and salted password
     */
    public String hash(final String password) {
        return hashingStrategy.hash("pbkdf2", // TODO: Is there a better option for Vert.X ? [CY-5601]
                null,
                // The salt is stored at the beginning of the resulting hash.
                // This way the salt can be changed without invalidating all previous hashes.
                Base64.getMimeEncoder().encodeToString(salt),
                password);
    }
}
