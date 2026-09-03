/**
 * LookupCache.java
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Thread-safe cache for distinct database lookup queries (such as higher taxa,
 * countries, primary divisions, collectors, and number types) to avoid
 * repetitive full-table scans.
 */
public class LookupCache {

	private static final Map<String, String[]> cache = new ConcurrentHashMap<>();

	public static String[] getOrLoad(String key, Supplier<String[]> loader) {
		return cache.computeIfAbsent(key, k -> loader.get());
	}

	public static void invalidate(String key) {
		cache.remove(key);
	}

	public static void clear() {
		cache.clear();
	}
}
