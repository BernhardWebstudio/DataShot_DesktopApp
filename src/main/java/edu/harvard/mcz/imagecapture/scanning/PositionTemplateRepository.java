/**
 * PositionTemplateRepository.java
 * edu.harvard.mcz.imagecapture.scanning
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
package edu.harvard.mcz.imagecapture.scanning;

import edu.harvard.mcz.imagecapture.PositionTemplate;
import java.util.List;
import java.util.Optional;

/**
 * Repository abstraction for loading {@link PositionTemplate} definitions
 * without coupling callers directly to static/database lookup helpers.
 */
public interface PositionTemplateRepository {

	/**
	 * Returns all component-bearing position templates in priority order.
	 *
	 * @return list of available templates
	 */
	List<PositionTemplate> findAll();

	/**
	 * Finds a template by its identifier, including special templates such as
	 * {@link PositionTemplate#TEMPLATE_NO_COMPONENT_PARTS}.
	 *
	 * @param templateId
	 *            template identifier
	 * @return matching template if found
	 */
	Optional<PositionTemplate> findById(String templateId);
}
