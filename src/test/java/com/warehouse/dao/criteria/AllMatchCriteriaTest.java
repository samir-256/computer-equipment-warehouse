package com.warehouse.dao.criteria;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.Samples;
import com.warehouse.common.Range;
import com.warehouse.entity.Laptop;
import org.junit.jupiter.api.Test;

class AllMatchCriteriaTest {

    private static final Laptop LAPTOP = Samples.laptop(7, "Acme", 1000.0, 3, 15.6);

    @Test
    void shouldMatchEverythingWithoutParameters() {
        assertTrue(new AllMatchCriteria<Laptop>().test(LAPTOP));
    }

    @Test
    void shouldMatchWhenAllParametersMatch() {
        SearchCriteria<Laptop> criteria = new AllMatchCriteria<Laptop>()
                .add(new BrandParameter("Acme"))
                .add(RangeParameter.price(new Range<>(null, 1200.0)))
                .add(RangeParameter.screenSize(new Range<>(15.0, null)));

        assertTrue(criteria.test(LAPTOP));
    }

    @Test
    void shouldNotMatchWhenOneParameterFails() {
        SearchCriteria<Laptop> criteria = new AllMatchCriteria<Laptop>()
                .add(new BrandParameter("Acme"))
                .add(RangeParameter.quantity(new Range<>(10, null)));

        assertFalse(criteria.test(LAPTOP));
    }

    @Test
    void shouldReturnSameCriteriaForChaining() {
        SearchCriteria<Laptop> criteria = new AllMatchCriteria<>();

        assertSame(criteria, criteria.add(Parameter.any()));
    }

    @Test
    void shouldRejectNullParameter() {
        assertThrows(NullPointerException.class, () -> new AllMatchCriteria<Laptop>().add(null));
    }

    @Test
    void shouldNotMatchWithNoneParameter() {
        assertFalse(new AllMatchCriteria<Laptop>().add(Parameter.none()).test(LAPTOP));
    }
}
