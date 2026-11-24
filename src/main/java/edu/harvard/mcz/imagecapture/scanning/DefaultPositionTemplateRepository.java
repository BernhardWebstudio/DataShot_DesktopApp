/**
 * DefaultPositionTemplateRepository.java
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
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link PositionTemplateRepository} delegating to
 * {@link PositionTemplate} while filtering
 * {@link PositionTemplate#TEMPLATE_NO_COMPONENT_PARTS} from {@link #findAll()}.
 */
public class DefaultPositionTemplateRepository implements PositionTemplateRepository {

	private static final Logger log = LoggerFactory.getLogger(DefaultPositionTemplateRepository.class);

	@Override
	public List<PositionTemplate> findAll() {
		List<String> templateIds = PositionTemplate.getTemplateIds();
		List<PositionTemplate> templates = new ArrayList<>();
		for (String templateId : templateIds) {
			if (templateId == null || PositionTemplate.TEMPLATE_NO_COMPONENT_PARTS.equals(templateId)) {
				continue;
			}
			findById(templateId).ifPresent(templates::add);
		}
		if (templates.isEmpty()) {
			findById(PositionTemplate.TEMPLATE_TEST_1).ifPresent(templates::add);
			findById(PositionTemplate.TEMPLATE_DEFAULT).ifPresent(templates::add);
		}
		return Collections.unmodifiableList(templates);
	}

	@Override
	public Optional<PositionTemplate> findById(String templateId) {
		if (templateId == null || templateId.isBlank()) {
			return Optional.empty();
		}
		try {
			return Optional.of(new PositionTemplate(templateId));
		} catch (NoSuchTemplateException e) {
			log.debug("Template '{}' not found: {}", templateId, e.getMessage());
			return Optional.empty();
		}
	}
}
