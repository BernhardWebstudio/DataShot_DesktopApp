/**
 * CollectionReturner.java
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
 *
 */
interface CollectionReturner {
    /**
     * Get the Collection from which a collection object came.
     *
     * @return A text string representing the name of the collection.
     */
    val collection: String?
}
