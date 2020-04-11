/**
 * MCZbaseGeogAuthRec.java
 * edu.harvard.mcz.imagecapture.data
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
 *
 *
 */
package edu.harvard.mcz.imagecapture.entity


import edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRec
import org.apache.commons.logging.LogFactory

/**
 * Proxy object for MCZbase/Arctos GEOG_AUTH_REC table
 * (higher geography authority file)
 *
 *
 * CREATE TABLE MCZBASE_GEOG_AUTH_REC (
 * GEOG_AUTH_REC_ID bigint NOT NULL primary key auto_increment,
 * CONTINENT_OCEAN VARCHAR(70),
 * COUNTRY VARCHAR(70),
 * STATE_PROV VARCHAR(75),
 * COUNTY VARCHAR(50),
 * QUAD VARCHAR(30),
 * FEATURE VARCHAR(50),
 * ISLAND VARCHAR(50),
 * ISLAND_GROUP VARCHAR(50),
 * SEA VARCHAR(50),
 * VALID_CATALOG_TERM_FG int NOT NULL,
 * SOURCE_AUTHORITY VARCHAR(45) NOT NULL,
 * HIGHER_GEOG VARCHAR(255),
 * OCEAN_REGION VARCHAR(50),
 * OCEAN_SUBREGION VARCHAR(50)
 * );
 */
class MCZbaseGeogAuthRec {
    /**
     * @return the geog_auth_rec_id
     */
    /**
     * @param geog_auth_rec_id the geog_auth_rec_id to set
     */
    var geog_auth_rec_id // GEOG_AUTH_REC_ID
            : Long? = null
    /**
     * @return the continent_ocean
     */
    /**
     * @param continent_ocean the continent_ocean to set
     */
    var continent_ocean // CONTINENT_OCEAN VARCHAR(70),
            : String? = null
    /**
     * @return the country
     */
    /**
     * @param country the country to set
     */
    var country // COUNTRY VARCHAR(70),
            : String? = null
    /**
     * @return the state_prov
     */
    /**
     * @param state_prov the state_prov to set
     */
    var state_prov // STATE_PROV VARCHAR(75),
            : String? = null
    /**
     * @return the county
     */
    /**
     * @param county the county to set
     */
    var county // COUNTY VARCHAR(50),
            : String? = null
    /**
     * @return the quad
     */
    /**
     * @param quad the quad to set
     */
    var quad // QUAD VARCHAR(30),
            : String? = null
    /**
     * @return the feature
     */
    /**
     * @param feature the feature to set
     */
    var feature // FEATURE VARCHAR(50),
            : String? = null
    /**
     * @return the island
     */
    /**
     * @param island the island to set
     */
    var island // ISLAND VARCHAR(50),
            : String? = null
    /**
     * @return the island_group
     */
    /**
     * @param island_group the island_group to set
     */
    var island_group // ISLAND_GROUP VARCHAR(50),
            : String? = null
    /**
     * @return the sea
     */
    /**
     * @param sea the sea to set
     */
    var sea // SEA VARCHAR(50),
            : String? = null
    /**
     * @return the valid_catalog_term_fg
     */
    /**
     * @param valid_catalog_term_fg the valid_catalog_term_fg to set
     */
    var valid_catalog_term_fg // VALID_CATALOG_TERM_FG int NOT NULL, = 0
    /**
     * @return the source_authority
     */
    /**
     * @param source_authority the source_authority to set
     */
    var source_authority // SOURCE_AUTHORITY VARCHAR(45) NOT NULL,
            : String? = null
    /**
     * @return the higher_geog
     */
    /**
     * @param higher_geog the higher_geog to set
     */
    var higher_geog // HIGHER_GEOG VARCHAR(255),
            : String? = null
    /**
     * @return the ocean_region
     */
    /**
     * @param ocean_region the ocean_region to set
     */
    var ocean_region // OCEAN_REGION VARCHAR(50),
            : String? = null
    /**
     * @return the ocean_subregion
     */
    /**
     * @param ocean_subregion the ocean_subregion to set
     */
    var ocean_subregion // OCEAN_SUBREGION VARCHAR(50)
            : String? = null

    override fun toString(): String {
        return if (higher_geog == null) {
            super.toString()
        } else {
            higher_geog!!
        }
    }

    companion object {
        private val log = LogFactory.getLog(MCZbaseGeogAuthRec::class.java)
    }
}
