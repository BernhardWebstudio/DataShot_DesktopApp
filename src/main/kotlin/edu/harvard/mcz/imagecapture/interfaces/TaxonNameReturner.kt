/**
 * TaxonNameReturner.java
 * edu.harvard.mcz.imagecapture.interfaces
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
package edu.harvard.mcz.imagecapture.interfaces


/**
 * Interface for classes that are able to return atomic taxon name components.
 * Intended as the return interface from a taxon name parser, and thus doesn't
 * include methods to set a string to parse or to parse the string, these can be
 * included in the instantiation of a concrete instance of a parser class, for
 * example:
 * TaxonNameReturner parser = new ConcreteTaxonNameParser(aStringToParse);
 * String authorship = parser.Authorship;
 */
interface TaxonNameReturner {
    /**
     * @return the authorship
     */
    val authorship: String?

    /**
     * @return the family
     */
    val family: String?

    /**
     * @return the subfamily
     */
    val subfamily: String?

    /**
     * @return the tribe
     */
    val tribe: String?

    /**
     * @return the genus
     */
    val genus: String?

    /**
     * @return the specificEpithet
     */
    val specificEpithet: String?

    /**
     * @return the subspecificEpithet
     */
    val subspecificEpithet: String?

    /**
     * @return the infraspecificEpithet
     */
    val infraspecificEpithet: String?

    /**
     * @return the infraspecificRank
     */
    val infraspecificRank: String?

    /**
     * @return the determiner
     */
    val identifiedBy: String?
}
