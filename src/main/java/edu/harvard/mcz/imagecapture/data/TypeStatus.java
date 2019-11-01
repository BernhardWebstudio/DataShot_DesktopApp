/**
 * TypeStatus.java
 * edu.harvard.mcz.imagecapture.data
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.data;

import edu.harvard.mcz.imagecapture.interfaces.ValueLister;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Controled Vocabulary for type status of specimens/names
 *
 *
 *
 */
public class TypeStatus implements ValueLister {
    private static final Log log = LogFactory.getLog(TypeStatus.class);

    public static String[] getTypeStatusValues() {
        String[] lifestages = {"Not a Type", "Holotype", "Paratype",
                "Lectotype", "Allotype", "Syntype",
                "Neotype", "Paralectotype", "Topotype",
                "Cotype", "Type"};
        return lifestages;
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.ValueLister#getValues()
     */
    @Override
    public String[] getValues() {
        return getTypeStatusValues();
    }
}
