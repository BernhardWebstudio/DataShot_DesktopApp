/**
 * NatureOfId.java
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
package edu.harvard.mcz.imagecapture.entity.fixed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class NatureOfId {
  public static final String LEGACY = "legacy";
  public static final String EXPERT_ID = "expert ID";
  private static final Logger log = LoggerFactory.getLogger(NatureOfId.class);

  public static String[] getNatureOfIdValues() {
    String[] values = {EXPERT_ID,
                       "type ID",
                       LEGACY,
                       "ID based on molecular data",
                       "ID to species group",
                       "erroneous citation",
                       "field ID",
                       "non-expert ID",
                       "taxonomic revision"};
    return values;
  }
}
