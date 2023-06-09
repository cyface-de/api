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
package de.cyface.api.v2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.cyface.api.v2.model.Role;

/**
 * Tests whether the roles constructions from database values works as expected.
 *
 * @author Armin Schnabel
 * @version 1.1.0
 * @since 1.0.0
 */
@SuppressWarnings({"SpellCheckingInspection"})
public class RoleTest {

    @ParameterizedTest
    @MethodSource("testParameters")
    void test_happyPath(final TestParameters parameters) {
        // Arrange

        // Act
        final var oocut = new Role(parameters.databaseValue);

        // Assert
        assertEquals(parameters.role, oocut);
    }

    @Test
    void test_managerWithoutGroup_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Role("_manager"));
    }

    @Test
    void test_groupWithoutType_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Role("project"));
    }

    @ParameterizedTest
    @MethodSource("testParameters")
    void testDatabaseIdentifier_happyPath(final TestParameters parameters) {
        // Arrange

        // Act
        final var oocut = parameters.role;

        // Assert
        assertEquals(parameters.databaseValue, oocut.databaseIdentifier());
    }

    @Test
    void testDatabaseIdentifier_userTypeWithEmptyGroup_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Role(Role.Type.GROUP_USER, ""));
    }

    @SuppressWarnings("unused")
    static Stream<TestParameters> testParameters() {
        return Stream.of(
                new TestParameters("guest", new Role(Role.Type.GUEST, null)),
                new TestParameters("project_user", new Role(Role.Type.GROUP_USER, "project")),
                new TestParameters("project_manager", new Role(Role.Type.GROUP_MANAGER, "project")),
                new TestParameters("pro-ject_manager", new Role(Role.Type.GROUP_MANAGER, "pro-ject")),
                new TestParameters("testGroup_manager", new Role(Role.Type.GROUP_MANAGER, "testGroup")));
    }

    private static class TestParameters {
        String databaseValue;
        Role role;

        public TestParameters(String databaseValue, Role role) {
            this.databaseValue = databaseValue;
            this.role = role;
        }
    }
}
