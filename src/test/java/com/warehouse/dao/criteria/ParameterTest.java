package com.warehouse.dao.criteria;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.Samples;
import com.warehouse.common.Range;
import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Equipment;
import com.warehouse.entity.Laptop;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ParameterTest {

    private static final Laptop LAPTOP = Samples.laptop(7, "Acme", 1000.0, 3, 15.6);

    @Test
    void rangeParameterNeedsRangeAndExtractor() {
        assertThrows(NullPointerException.class, () -> new RangeParameter<Laptop, Double>(null, Laptop::getPrice));
        assertThrows(NullPointerException.class,
                () -> new RangeParameter<Laptop, Double>(new Range<>(1.0, 2.0), null));
    }

    @Test
    void rangeParameterWorksWithAnyExtractor() {
        assertTrue(new RangeParameter<>(new Range<>(8, 16), Laptop::getRamGb).test(LAPTOP));
        assertFalse(new RangeParameter<>(new Range<>(16, 32), Laptop::getRamGb).test(LAPTOP));
    }

    @Test
    void shouldMatchOnlySameId() {
        assertTrue(new IdParameter(7).test(LAPTOP));
        assertFalse(new IdParameter(8).test(LAPTOP));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void shouldRejectNonPositiveId(int id) {
        assertThrows(IllegalArgumentException.class, () -> new IdParameter(id));
    }

    @Test
    void shouldMatchPartOfNameIgnoringCase() {
        assertTrue(new NameParameter("laptop").test(LAPTOP));
        assertTrue(new NameParameter("TOP 7").test(LAPTOP));
        assertFalse(new NameParameter("mouse").test(LAPTOP));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankName(String text) {
        assertThrows(IllegalArgumentException.class, () -> new NameParameter(text));
    }

    @Test
    void shouldRejectNullNameAndBrand() {
        assertThrows(IllegalArgumentException.class, () -> new NameParameter(null));
        assertThrows(IllegalArgumentException.class, () -> new BrandParameter(null));
    }

    @Test
    void shouldMatchBrandIgnoringCase() {
        assertTrue(new BrandParameter("acme").test(LAPTOP));
        assertTrue(new BrandParameter(" ACME ").test(LAPTOP));
        assertFalse(new BrandParameter("Acm").test(LAPTOP));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    void shouldRejectBlankBrand(String brand) {
        assertThrows(IllegalArgumentException.class, () -> new BrandParameter(brand));
    }

    @Test
    void shouldMatchPriceInsideRange() {
        assertTrue(RangeParameter.price(new Range<>(1000.0, 1000.0)).test(LAPTOP));
        assertTrue(RangeParameter.price(new Range<>(500.0, 1500.0)).test(LAPTOP));
        assertFalse(RangeParameter.price(new Range<>(1000.01, 2000.0)).test(LAPTOP));
        assertFalse(RangeParameter.price(new Range<>(null, 999.99)).test(LAPTOP));
    }

    @Test
    void shouldMatchQuantityInsideRange() {
        assertTrue(RangeParameter.quantity(new Range<>(3, 3)).test(LAPTOP));
        assertTrue(RangeParameter.quantity(new Range<>(1, null)).test(LAPTOP));
        assertFalse(RangeParameter.quantity(new Range<>(4, null)).test(LAPTOP));
    }

    @Test
    void shouldRejectNullRanges() {
        assertThrows(NullPointerException.class, () -> RangeParameter.price(null));
        assertThrows(NullPointerException.class, () -> RangeParameter.quantity(null));
        assertThrows(NullPointerException.class, () -> RangeParameter.<Laptop>screenSize(null));
    }

    @Test
    void shouldMatchScreenSizeOfLaptopsAndMonitors() {
        RangeParameter<Laptop, Double> forLaptops = RangeParameter.screenSize(new Range<>(15.0, 16.0));
        RangeParameter<com.warehouse.entity.Monitor, Double> forMonitors =
                RangeParameter.screenSize(new Range<>(24.0, null));

        assertTrue(forLaptops.test(LAPTOP));
        assertFalse(forLaptops.test(Samples.laptop(1, "Acme", 1.0, 1, 13.3)));
        assertTrue(forMonitors.test(Samples.monitor(2, 27.0)));
        assertFalse(forMonitors.test(Samples.monitor(3, 23.8)));
    }

    @Test
    void shouldMatchConnectionTypeOfMiceAndKeyboards() {
        ConnectionTypeParameter<com.warehouse.entity.Mouse> forMice =
                new ConnectionTypeParameter<>(ConnectionType.WIRELESS);
        ConnectionTypeParameter<com.warehouse.entity.Keyboard> forKeyboards =
                new ConnectionTypeParameter<>(ConnectionType.WIRED);

        assertTrue(forMice.test(Samples.mouse(1, "Acme", 10.0, 1, ConnectionType.WIRELESS)));
        assertFalse(forMice.test(Samples.mouse(2, "Acme", 10.0, 1, ConnectionType.WIRED)));
        assertTrue(forKeyboards.test(Samples.keyboard(3, ConnectionType.WIRED)));
        assertFalse(forKeyboards.test(Samples.keyboard(4, ConnectionType.WIRELESS)));
    }

    @Test
    void shouldRejectNullConnectionType() {
        assertThrows(NullPointerException.class, () -> new ConnectionTypeParameter<com.warehouse.entity.Mouse>(null));
    }

    @Test
    void shouldAcceptAnyOrNoEquipment() {
        Parameter<Equipment> any = Parameter.any();
        Parameter<Equipment> none = Parameter.none();

        assertTrue(any.test(LAPTOP));
        assertFalse(none.test(LAPTOP));
    }

    @Test
    void shouldApplyCommonParametersToEveryEntityType() {
        Parameter<Equipment> acme = new BrandParameter("Acme");

        assertTrue(acme.test(LAPTOP));
        assertTrue(acme.test(Samples.mouse(1, "Acme", 10.0, 1, ConnectionType.WIRED)));
        assertTrue(acme.test(Samples.keyboard(2, ConnectionType.WIRED)));
    }
}
