/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd.attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndElement;
import org.jboss.tools.modeshape.jcr.cnd.Constants;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class SuperTypesTest implements Constants {

    private SuperTypes attribute;

    private void add( String item ) {
        if (!this.attribute.add(item)) {
            fail();
        }
    }

    private void remove( String item ) {
        if (!this.attribute.remove(item)) {
            fail();
        }
    }

    @Before
    public void beforeEach() {
        this.attribute = new SuperTypes();
    }

    @Test
    public void initialCndNotationShouldBeEmpty() {
        assertTrue(Utils.isEmpty(this.attribute.toCndNotation(CndElement.NotationType.LONG)));
        assertTrue(Utils.isEmpty(this.attribute.toCndNotation(CndElement.NotationType.COMPRESSED)));
        assertTrue(Utils.isEmpty(this.attribute.toCndNotation(CndElement.NotationType.COMPACT)));
    }

    @Test
    public void verifyAddedItem() {
        // setup
        add(ITEM_ONE);

        // tests
        assertEquals(1, this.attribute.getSupportedItems().size());
        assertTrue(this.attribute.getSupportedItems().contains(ITEM_ONE));
    }

    @Test
    public void verifyInitiallyNoSupportedItems() {
        assertEquals(0, this.attribute.getSupportedItems().size());
    }

    @Test
    public void verifyInitialStateShouldBeIsNot() {
        assertEquals(AttributeState.Value.IS_NOT, this.attribute.get());
    }

    @Test
    public void verifyMultipleElementsCndNotation() {
        // setup
        add(ITEM_ONE);
        add(ITEM_TWO);
        add(ITEM_THREE);

        // tests
        assertEquals(SUPER_TYPES_THREE_ITEM_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPACT));
        assertEquals(SUPER_TYPES_THREE_ITEM_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPRESSED));
        assertEquals(SUPER_TYPES_THREE_ITEM_FORM, this.attribute.toCndNotation(CndElement.NotationType.LONG));
    }

    @Test
    public void verifyOneElementCndNotation() {
        // setup
        add(ITEM_ONE);

        // tests
        assertEquals(SUPER_TYPES_ONE_ITEM_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPACT));
        assertEquals(SUPER_TYPES_ONE_ITEM_FORM, this.attribute.toCndNotation(CndElement.NotationType.COMPRESSED));
        assertEquals(SUPER_TYPES_ONE_ITEM_FORM, this.attribute.toCndNotation(CndElement.NotationType.LONG));
    }

    @Test
    public void verifyRemoveItem() {
        // setup
        add(ITEM_ONE);
        add(ITEM_TWO);
        remove(ITEM_ONE);

        // tests
        assertFalse(this.attribute.getSupportedItems().contains(ITEM_ONE));
    }

    @Test
    public void verifySameElementIsNotAdded() {
        // setup
        add(ITEM_ONE);

        // tests
        if (this.attribute.add(ITEM_ONE)) {
            fail();
        }

        assertEquals(1, this.attribute.getSupportedItems().size());
    }

    @Test
    public void verifyStateShouldBeIsAfterAdd() {
        // setup
        add(ITEM_ONE);

        // tests
        assertEquals(AttributeState.Value.IS, this.attribute.get());
    }

    @Test
    public void verifyStateShouldBeIsNotWhenEmpty() {
        // setup
        add(ITEM_ONE);
        remove(ITEM_ONE);

        // tests
        assertEquals(0, this.attribute.getSupportedItems().size());
        assertEquals(AttributeState.Value.IS_NOT, this.attribute.get());
    }

    @Test
    public void verifyVariantCndNotation() {
        // setup
        this.attribute.set(AttributeState.Value.VARIANT);

        // tests
        assertEquals(SUPER_TYPES_VARIANT, this.attribute.toCndNotation(CndElement.NotationType.COMPACT));
        assertEquals(SUPER_TYPES_VARIANT, this.attribute.toCndNotation(CndElement.NotationType.COMPRESSED));
        assertEquals(SUPER_TYPES_VARIANT, this.attribute.toCndNotation(CndElement.NotationType.LONG));
    }

}
