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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.vertx.ext.auth.HashingStrategy;

/**
 * @author Armin Schnabel
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings({"SpellCheckingInspection"})
public class HasherTest {

    /**
     * [DAT-624] The password hashes generated by the `management-API` on S2 have an old format: `F96C2614.....A167`.
     * The hashes generated with the current `Hashes` have a new format: `$pbkdf2$U1VHQVI....`. Both work!
     */
    @Test
    void testHash() {
        // Arrange
        final var salt = "SUGAR";
        final var oocut = new Hasher(HashingStrategy.load(), salt.getBytes(StandardCharsets.UTF_8));

        // Act
        final var password = "secret";
        final var result = oocut.hash(password);

        // Assert
        assertThat("Compare generated hashcode", result, is(equalTo(
                "$pbkdf2$U1VHQVI=$ZK4ZDOf9i3AibLO23RwmTmwfSe4qCQl0Mxl1zSPPW1+tF593v3Ip5RjiWU8j6M251AYjic8V/lhLsxukCpi/Ig")));
    }
}
