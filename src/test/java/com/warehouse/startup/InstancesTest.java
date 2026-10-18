package com.warehouse.startup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class InstancesTest {

    @Test
    void createsObjectWithNoArguments() {
        List<?> list = Instances.create("java.util.ArrayList", List.class);

        assertEquals(new ArrayList<>(), list);
    }

    @Test
    void createsObjectWithConstructorArgument() {
        CharSequence text = Instances.create("java.lang.StringBuilder", CharSequence.class, "abc");

        assertEquals("abc", text.toString());
    }

    @Test
    void unknownClassIsReported() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> Instances.create("com.warehouse.NoSuchClass", Object.class));

        assertTrue(error.getMessage().contains("com.warehouse.NoSuchClass"));
    }

    @Test
    void wrongTypeIsReported() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> Instances.create("java.lang.String", List.class));

        assertTrue(error.getMessage().contains("java.lang.String"));
        assertTrue(error.getMessage().contains("List"));
    }

    @Test
    void missingConstructorIsReported() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> Instances.create("java.util.ArrayList", List.class, "not a number"));

        assertTrue(error.getMessage().contains("no public constructor"));
    }

    @Test
    void failingConstructorKeepsTheCause() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> Instances.create("com.warehouse.startup.SourceFailingConstructor", Object.class, "x.csv"));

        assertTrue(error.getMessage().contains("broken source x.csv"));
        assertTrue(error.getCause() instanceof IllegalArgumentException);
    }
}
