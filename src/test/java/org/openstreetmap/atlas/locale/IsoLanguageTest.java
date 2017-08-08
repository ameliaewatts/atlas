package org.openstreetmap.atlas.locale;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for IsoLanguage
 *
 * @author robert_stack
 */
public class IsoLanguageTest
{
    @Test
    public void testLanguage()
    {
        Assert.assertEquals(Optional.empty(), IsoLanguage.displayLanguageForLanguageCode(null));
        Assert.assertEquals(Optional.empty(), IsoLanguage.displayLanguageForLanguageCode("zz"));

        Assert.assertFalse(IsoLanguage.isValidLanguageCode(null));
        Assert.assertTrue(IsoLanguage.isValidLanguageCode("es"));
        Assert.assertFalse(IsoLanguage.isValidLanguageCode("zz"));
        Assert.assertEquals("Spanish", IsoLanguage.displayLanguageForLanguageCode("es").get());

        Assert.assertFalse(IsoLanguage.isValidDisplayLanguage(null));
        Assert.assertFalse(IsoLanguage.isValidDisplayLanguage("NonexistentCountry"));
        Assert.assertTrue(IsoLanguage.isValidDisplayLanguage("Spanish"));

        final Optional<IsoLanguage> isoLanguage1 = IsoLanguage.forLanguageCode("es");
        Assert.assertEquals("es", isoLanguage1.get().getLanguageCode());
        final Optional<IsoLanguage> isoLanguage2 = IsoLanguage.forLanguageCode("zz");
        Assert.assertEquals(Optional.empty(), isoLanguage2);
        final Optional<IsoLanguage> isoLanguage3 = IsoLanguage.forLanguageCode("es");
        Assert.assertEquals("Spanish", isoLanguage3.get().getDisplayLanguage());

        Assert.assertTrue(IsoLanguage.allLanguageCodes().contains("ar"));
    }
}
