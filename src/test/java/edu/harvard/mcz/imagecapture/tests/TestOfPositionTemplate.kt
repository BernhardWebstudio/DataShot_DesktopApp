/**
 * TestOfPositionTemplate.java
 * edu.harvard.mcz.imagecapture.tests
 *
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package edu.harvard.mcz.imagecapture.tests

import edu.harvard.mcz.imagecapture.ImageCaptureApp
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.entity.Template
import edu.harvard.mcz.imagecapture.exceptions.BadTemplateException
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException
import junit.framework.TestCase
import org.junit.experimental.categories.Category

/**
 * TestOfPositionTemplate tests the construction of PositionTemplate and the internal
 * consistency of the list of templates provided by PositionTemplate and the default template
 * known to the Singleton.
 */
@Category(IntegrationTest::class)
class TestOfPositionTemplate
/**
 * @param name
 */(name: String?) : TestCase(name) {
    /**
     * Test method for [edu.harvard.mcz.imagecapture.PositionTemplate.PositionTemplate].
     */
    fun testPositionTemplate() {
        val t = PositionTemplate()
        assertEquals(t.getName(), PositionTemplate().getName())
    }

    /**
     * Test method for [edu.harvard.mcz.imagecapture.PositionTemplate.PositionTemplate].
     */
    fun testPositionTemplateString() { // Test exceptions on a bad template name.
        try {
            val badTemplate = PositionTemplate("random string that isn't a template name... 123")
            badTemplate.getClass() // // added to suppress FindBugs DLS_DEAD_LOCAL_STORE
            fail("Failed to throw NoSuchTemplateException")
        } catch (e: NoSuchTemplateException) { // pass
        } catch (ex: NullPointerException) { // TODO: Mock object?
// db connection failed
        }
        // Test normal template construction
        var t: PositionTemplate? = null
        try {
            t = PositionTemplate(PositionTemplate.TEMPLATE_DEFAULT)
            var t1: PositionTemplate? = null
            try {
                t1 = PositionTemplate(PositionTemplate.TEMPLATE_TEST_1)
                assertNotSame(t.getName(), t1.getName())
            } catch (e: NoSuchTemplateException) {
                fail("Threw unexpected NoSuchTemplateException")
            }
        } catch (e: NoSuchTemplateException) {
            fail("Threw unexpected NoSuchTemplateException")
        }
        // Test the special case of TEMPLATE_NO_COMPONENT_PARTS
        try {
            t = PositionTemplate(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)
            assertEquals(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS, t.getTemplateId())
        } catch (e: NoSuchTemplateException) {
            fail("Threw unexpected NoSuchTemplateException")
        }
    }

    fun testAllTemplatesInList() {
        val templates: List<String> = PositionTemplate.getTemplateIds()
        val i = templates.listIterator()
        var foundNoComponentParts = false
        while (i.hasNext()) {
            try {
                val template = PositionTemplate(i.next())
                if (template.getTemplateId().equals(PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS)) {
                    foundNoComponentParts = true
                }
            } catch (e: NoSuchTemplateException) {
                fail("Threw NoSuchTemplate exception for a template on the list of templates." + e.getMessage())
            }
        }
        if (foundNoComponentParts == false) {
            fail("TEMPLATE_NO_COMPONENT_PARTS was not on the list of templates.")
        }
    }

    fun testDefaultPositionTemplateInSingleton() {
        try {
            val defaultTemplate = PositionTemplate(Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_DEFAULT_TEMPLATES))
            defaultTemplate.getClass() // added to suppress FindBugs DLS_DEAD_LOCAL_STORE
        } catch (e: NoSuchTemplateException) {
            fail("Default Position Template returned from Singelton Doesn't exist. " + e.getMessage())
        }
    }

    fun testTemplatePositionTemplateRoundTrip() { // Store values to all fields in a Template
// Store into PositionTemplate, and back to a New Template
// Then compare the two templates.
        val t = Template()
        t.setName("Test")
        t.setTemplateId("test")
        t.setImageSizeX(100)
        t.setImageSizeY(200)
        t.setBarcodePositionX(3)
        t.setBarcodePositionY(4)
        t.setBarcodeSizeX(5)
        t.setBarcodeSizeY(6)
        t.setLabelPositionX(7)
        t.setLabelPositionY(8)
        t.setLabelSizeX(9)
        t.setLabelSizeY(10)
        t.setUtLabelPositionX(11)
        t.setUtLabelPositionY(12)
        t.setUtLabelSizeX(13)
        t.setUtLabelSizey(14)
        t.setSpecimenPositionX(15)
        t.setSpecimenPositionY(16)
        t.setSpecimenSizeX(17)
        t.setSpecimenSizeY(18)
        t.setTextPositionX(19)
        t.setTextPositionY(20)
        t.setTextSizeX(21)
        t.setTextSizeY(22)
        t.setUtBarcodePositionX(23)
        t.setUtBarcodePositionY(24)
        t.setUtBarcodeSizeX(25)
        t.setUtBarcodeSizeY(26)
        val pt = PositionTemplate(t)
        val t1 = Template()
        try {
            pt.populateTemplateFromPositionTemplate(t1)
        } catch (e: BadTemplateException) {
            fail("BadTemplateException thrown on populating Template from valid PositionTemplate.")
        }
        assertEquals(t.getName(), t1.getName())
        assertEquals(t.getImageSizeX(), t.getImageSizeX())
        assertEquals(t.getImageSizeY(), t.getImageSizeY())
        assertEquals(t.getBarcodePositionX(), t1.getBarcodePositionX())
        assertEquals(t.getBarcodePositionY(), t1.getBarcodePositionY())
        assertEquals(t.getBarcodeSizeX(), t1.getBarcodeSizeX())
        assertEquals(t.getBarcodeSizeY(), t1.getBarcodeSizeY())
        assertEquals(t.getTextPositionX(), t1.getTextPositionX())
        assertEquals(t.getTextPositionY(), t1.getTextPositionY())
        assertEquals(t.getTextSizeX(), t1.getTextSizeX())
        assertEquals(t.getTextSizeY(), t1.getTextSizeY())
        assertEquals(t.getLabelPositionX(), t1.getLabelPositionX())
        assertEquals(t.getLabelPositionY(), t1.getLabelPositionY())
        assertEquals(t.getLabelSizeX(), t1.getLabelSizeX())
        assertEquals(t.getLabelSizeY(), t1.getLabelSizeY())
        assertEquals(t.getUtLabelPositionX(), t1.getUtLabelPositionX())
        assertEquals(t.getUtLabelPositionY(), t1.getUtLabelPositionY())
        assertEquals(t.getUtLabelSizeX(), t1.getUtLabelSizeX())
        assertEquals(t.getUtLabelSizey(), t1.getUtLabelSizey())
        assertEquals(t.getSpecimenPositionX(), t1.getSpecimenPositionX())
        assertEquals(t.getSpecimenPositionY(), t1.getSpecimenPositionY())
        assertEquals(t.getSpecimenSizeX(), t1.getSpecimenSizeX())
        assertEquals(t.getSpecimenSizeY(), t1.getSpecimenSizeY())
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        // Create an instance of MainFrame to support database connection
        ImageCaptureApp.main(null)
        // hide it so user can't see it.
        Singleton.getSingletonInstance().getMainFrame().setVisible(false)
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
        // reference the MainFrame again so it won't be disposed of until these tests are done.
        Singleton.getSingletonInstance().getMainFrame().setVisible(false)
    }
}