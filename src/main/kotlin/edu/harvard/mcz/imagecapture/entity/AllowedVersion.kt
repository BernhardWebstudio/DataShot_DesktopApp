/**
 * AllowedVersion.java
 *
 *
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


import edu.harvard.mcz.imagecapture.entity.AllowedVersion
import org.apache.commons.logging.LogFactory

// import javax.persistence.Basic;
// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.Id;
// import javax.persistence.NamedQueries;
// import javax.persistence.NamedQuery;
// import javax.persistence.SequenceGenerator;
// import javax.persistence.Table;
/**
 *
 */
//@Entity
//@Table(name = "allowed_version", catalog = "lepidoptera", schema = "")
//@NamedQueries({
//    @NamedQuery(name = "AllowedVersion.findAll", query = "SELECT a FROM
//    AllowedVersion a")
//    })
class AllowedVersion {
    /**
     * @return the allowedVersionId
     */
    /**
     * @param allowedVersionId the allowedVersionId to set
     */
    //@Id
//@GeneratedValue(generator="AllowedNumberSeq")
//@SequenceGenerator(name="AllowedNumberSeq",sequenceName="SEQ_ALLOWEDVERSIONID",
//allocationSize=1)
//@Basic(optional = false)
//@Column(name = "allowed_version_id", nullable = false)
    var allowedVersionId: Long? = null
    /**
     * @return the version
     */
    /**
     * @param version the version to set
     */
    //@Column(name="version", nullable=false)
    var version: String? = null

    companion object {
        private val log = LogFactory.getLog(AllowedVersion::class.java)
    }
}
