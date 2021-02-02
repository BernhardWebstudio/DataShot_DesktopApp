/**
 * AllowedVersion.java
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
package edu.harvard.mcz.imagecapture.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
//@Entity
//@Table(name = "allowed_version", catalog = "lepidoptera", schema = "")
//@NamedQueries({
//    @NamedQuery(name = "AllowedVersion.findAll", query = "SELECT a FROM
//    AllowedVersion a")
//    })
public class AllowedVersion {

    private static final Logger log =
            LoggerFactory.getLogger(AllowedVersion.class);

    //@Id
    //@GeneratedValue(generator="AllowedNumberSeq")
    //@SequenceGenerator(name="AllowedNumberSeq",sequenceName="SEQ_ALLOWEDVERSIONID",
    // allocationSize=1)
    //@Basic(optional = false)
    //@Column(name = "allowed_version_id", nullable = false)
    private Long allowedVersionId;

    //@Column(name="version", nullable=false)
    private String version;

    /**
     * @return the allowedVersionId
     */
    public Long getAllowedVersionId() {
        return allowedVersionId;
    }

    /**
     * @param allowedVersionId the allowedVersionId to set
     */
    public void setAllowedVersionId(Long allowedVersionId) {
        this.allowedVersionId = allowedVersionId;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
