/**
 * ConnectionException.java
 * <p>
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
package edu.harvard.mcz.imagecapture.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 */
public class ConnectionException extends Exception {

    private static final long serialVersionUID = 5218215421281921931L;

    private static final Log log = LogFactory.getLog(ConnectionException.class);

    /**
     *
     */
    public ConnectionException() {
        log.debug(ConnectionException.class.getName());
    }

    /**
     * @param message
     */
    public ConnectionException(String message) {
        super(message);
        log.debug(message);
    }

    /**
     * @param cause
     */
    public ConnectionException(Throwable cause) {
        super(cause);
        log.debug(cause.toString());
    }

    /**
     * @param message
     * @param cause
     */
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
        log.debug(message);
    }
}
