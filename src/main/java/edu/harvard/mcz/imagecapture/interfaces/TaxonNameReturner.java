/**
 * TaxonNameReturner.java
 * edu.harvard.mcz.imagecapture.interfaces
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
package edu.harvard.mcz.imagecapture.interfaces;

/**
 * Interface for classes that are able to return atomic taxon name components. 
 * Intended as the return interface from a taxon name parser, and thus doesn't
 * include methods to set a string to parse or to parse the string, these can be
 * included in the instantiation of a concrete instance of a parser class, for
 * example:
 * TaxonNameReturner parser = new ConcreteTaxonNameParser(aStringToParse);
 * String authorship = parser.getAuthorship();
 *
 *
 *
 */
public interface TaxonNameReturner {

    /**
     * @return the authorship
     */
    String getAuthorship();

    /**
     * @return the family
     */
    String getFamily();

    /**
     * @return the subfamily
     */
    String getSubfamily();

    /**
     * @return the tribe
     */
    String getTribe();

    /**
     * @return the genus
     */
    String getGenus();

    /**
     * @return the specificEpithet
     */
    String getSpecificEpithet();

    /**
     * @return the subspecificEpithet
     */
    String getSubspecificEpithet();

    /**
     * @return the infraspecificEpithet
     */
    String getInfraspecificEpithet();

    /**
     * @return the infraspecificRank
     */
    String getInfraspecificRank();

    /**
     * @return the determiner
     */
    String getIdentifiedBy();

}