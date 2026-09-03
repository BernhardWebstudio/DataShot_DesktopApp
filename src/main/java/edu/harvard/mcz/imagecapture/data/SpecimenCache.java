/**
 * SpecimenCache.java
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

import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LRU cache and speculative pre-fetcher for Specimen records.
 * Keeps recently visited or hovered specimen records in memory to provide
 * instantaneous details view pane loading.
 */
public class SpecimenCache {

	private static final Logger log = LoggerFactory.getLogger(SpecimenCache.class);
	private static final int MAX_CACHE_SIZE = 50;

	private static final Map<Long, Specimen> cache = new LinkedHashMap<Long, Specimen>(MAX_CACHE_SIZE, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<Long, Specimen> eldest) {
			return size() > MAX_CACHE_SIZE;
		}
	};

	private static final ExecutorService PREFETCH_EXECUTOR = Executors.newFixedThreadPool(2, r -> {
		Thread t = new Thread(r, "SpecimenPrefetch-Worker");
		t.setDaemon(true);
		return t;
	});

	public static synchronized Specimen get(Long specimenId) {
		if (specimenId == null) {
			return null;
		}
		Specimen s = cache.get(specimenId);
		if (s != null && !s.isFullyLoaded()) {
			cache.remove(specimenId);
			return null;
		}
		return s;
	}

	public static synchronized void put(Specimen specimen) {
		if (specimen != null && specimen.getSpecimenId() != null && specimen.isFullyLoaded()) {
			cache.put(specimen.getSpecimenId(), specimen);
		}
	}

	public static synchronized void invalidate(Long specimenId) {
		if (specimenId != null) {
			cache.remove(specimenId);
		}
	}

	public static synchronized void clear() {
		cache.clear();
	}

	/**
	 * Asynchronously pre-fetches the complete Specimen entity by ID in a background daemon thread
	 * if it is not already present in the cache.
	 *
	 * @param specimenId the ID of the specimen to prefetch
	 */
	public static void prefetchAsync(Long specimenId) {
		if (specimenId == null) {
			return;
		}
		synchronized (SpecimenCache.class) {
			if (cache.containsKey(specimenId)) {
				return;
			}
		}
		PREFETCH_EXECUTOR.submit(() -> {
			try {
				SpecimenLifeCycle sls = new SpecimenLifeCycle();
				Specimen specimen = sls.findById(specimenId);
				if (specimen != null && specimen.isFullyLoaded()) {
					put(specimen);
					log.debug("Speculatively prefetched specimen id: {}", specimenId);
				}
			} catch (Exception e) {
				log.debug("Prefetch failed for specimen id {}: {}", specimenId, e.getMessage());
			}
		});
	}
}
