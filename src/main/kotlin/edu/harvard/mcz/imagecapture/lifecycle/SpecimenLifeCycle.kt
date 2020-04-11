/**
 *
 */
package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.SpecimenPart
import edu.harvard.mcz.imagecapture.entity.Tracking
import edu.harvard.mcz.imagecapture.entity.fixed.CountValue
import edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCount
import edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCount
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.exceptions.ConnectionException
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.*
import org.hibernate.criterion.Example
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.hibernate.exception.ConstraintViolationException
import java.math.BigDecimal
import java.util.*
import javax.persistence.PersistenceException

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class Specimen.
 * @see Specimen
 */
class SpecimenLifeCycle : GenericLifeCycle<Specimen?>(Specimen::class.java, log) {
    /**Log who changed this specimen when and what its current status is to the
     * tracking table.
     *
     * @param specimen the specimen for which the change is to be logged to the tracking table.
     */
    protected fun track(specimen: Specimen) {
        val t = Tracking(
                specimen, Singleton.getUserFullName(), specimen.getWorkFlowStatus(), Date()
        )
        val tls = TrackingLifeCycle()
        try {
            tls.persist(t)
        } catch (e: SaveFailedException) { // TODO Handle save error in UI
            log!!.error(e)
        }
    }

    /** Save a new specimen record, and add an entry in the tracking table.
     *
     * @param transientInstance instance of a Specimen that doesn't have a matching
     * database record which is to be saved as a new record in the database.
     *
     * @throws SaveFailedException
     * @throws SpecimenExistsException
     */
    @Throws(SaveFailedException::class, SpecimenExistsException::class)
    fun persist(transientInstance: Specimen) {
        log!!.debug("persisting Specimen instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.persist(transientInstance)
                session.transaction.commit()
                log!!.debug("persist successful")
                track(transientInstance)
            } catch (e: PersistenceException) {
                session.transaction.rollback()
                try {
                    session.close()
                } catch (se: SessionException) {
                }
                val message: String = e.message
                var pmessage = ""
                try {
                    pmessage = e.cause.message
                } catch (ex: Exception) { // expected if e doesn't have a cause.
                }
                log!!.error("persist failed", e)
                val existsPattern = "^Duplicate entry '.*' for key 'Barcode'$"
                if (message.matches(existsPattern) || pmessage.matches(existsPattern) || e is ConstraintViolationException) {
                    log!!.debug("specimen exists")
                    throw SpecimenExistsException("Specimen record exists for " + transientInstance.getBarcode())
                } else {
                    log!!.debug("specimen save failed")
                    throw SaveFailedException("Unable to save specimen " + transientInstance.getBarcode())
                }
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (see: SpecimenExistsException) { // Pass on upwards unchanged
            log!!.error("persist failed see", see)
            throw see
        } catch (see: SaveFailedException) {
            log!!.error("persist failed see", see)
            throw see
        } catch (re: RuntimeException) { // Catch, log, and pass on any other exception.
            log!!.error("persist failed", re)
            throw re
        }
    }

    /**
     * Save or update an existing specimen record.
     *
     * @param instance of a Specimen that that is to be saved.
     * @throws SaveFailedException
     */
    @Throws(SaveFailedException::class)
    fun attachDirty(instance: Specimen) {
        log!!.debug("attaching dirty Specimen instance with id " + instance.getSpecimenId())
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.saveOrUpdate(instance)
                session.transaction.commit()
                log!!.debug("attach successful")
                track(instance)
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("attach failed", e)
                throw SaveFailedException("Unable to save specimen " + instance.getBarcode())
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error("attach failed", re)
            throw re
        }
    }

    /**
     * Re-associate a transient instance with a session.
     *
     * @param instance
     */
    fun attachClean(instance: Specimen) {
        try {
            log!!.debug(instance.getSpecimenParts().size)
            log!!.debug((instance.getSpecimenParts().toTypedArray().get(0) as SpecimenPart).getPartAttributeValuesConcat())
        } catch (e: Exception) {
            log!!.debug(e.message)
        }
        log!!.debug("attaching clean Specimen instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.lock(instance, LockMode.NONE)
                session.flush()
                session.transaction.commit()
                log!!.debug("attach successful")
                log!!.debug(instance.getSpecimenParts().size)
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.debug(e.message, e)
                log!!.debug("Trying to attach dirty")
                try {
                    attachDirty(instance)
                } catch (e1: SaveFailedException) {
                    log!!.error("attach failed", e1)
                    e1.printStackTrace()
                }
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error("attach failed", re)
            throw re
        }
        try {
            log!!.debug(instance.getSpecimenParts().size)
            log!!.debug((instance.getSpecimenParts().toTypedArray().get(0) as SpecimenPart).getPartAttributeValuesConcat())
        } catch (e: Exception) {
            log!!.debug(e.message)
        }
    }

    fun delete(persistentInstance: Specimen?) {
        log!!.debug("deleting Specimen instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.delete(persistentInstance)
                session.transaction.commit()
                log!!.debug("delete successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("delete failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error("delete failed", re)
            throw re
        }
    }

    /** Update db record and log current status of record.
     *
     * @param detachedInstance
     * @return the current specimen record.
     */
    fun merge(detachedInstance: Specimen?): Specimen {
        log!!.debug("merging Specimen instance")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                val result: Specimen = session.merge(detachedInstance) as Specimen
                session.transaction.commit()
                log!!.debug("merge successful")
                try {
                    session.close()
                } catch (e: SessionException) {
                }
                track(result)
                result
            } catch (e: HibernateException) {
                session.transaction.rollback()
                try {
                    session.close()
                } catch (e1: SessionException) {
                }
                log!!.error("merge failed", e)
                throw e
            }
        } catch (re: RuntimeException) {
            log!!.error("merge failed", re)
            throw re
        }
    }

    fun findById(id: Long?): Specimen? {
        log!!.debug("getting Specimen instance with id: $id")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var instance: Specimen? = null
            try {
                instance = session.get<Specimen?>(Specimen::class.java, id)
                session.transaction.commit()
                if (instance == null) {
                    log!!.debug("get successful, no instance found")
                } else {
                    log!!.debug("get successful, instance found")
                    log!!.debug(instance.getSpecimenParts().size)
                }
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("get failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            instance
        } catch (re: RuntimeException) {
            log!!.error("get failed", re)
            throw re
        }
    }

    fun findByBarcode(barcode: String?): MutableList<Specimen?>? {
        log!!.debug("findByBarcode '$barcode' start")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<Specimen?>? = null
            try {
                val query = session.createQuery("From Specimen as s where s.barcode = ?1")
                query!!.setParameter(1, barcode)
                results = query.list() as MutableList<Specimen?>
                log!!.debug("find query successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("findByBarcode failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("findByBarcode failed.  ", re)
            throw re
        }
    }

    fun findAll(): MutableList<Specimen?>? {
        log!!.debug("finding all Specimens")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<Specimen?>? = null
            try {
                results = session.createQuery("From Specimen s order by s.barcode").list() as MutableList<Specimen?>
                log!!.debug("find all successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("find all failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("Find all failed.  ", re)
            throw re
        }
    }

    fun findAllPage(startAt: Int, fetchSize: Int): MutableList<Specimen?>? {
        log!!.debug("finding $fetchSize Specimens from $startAt.")
        return try {
            if (startAt < 0 || fetchSize < 0) {
                throw RuntimeException("Negative value given for page size")
            }
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<Specimen?>? = null
            try {
                val query = session.createQuery("From Specimen s order by s.barcode")
                query!!.firstResult = startAt
                query.setFetchSize(fetchSize)
                results = query.list() as MutableList<Specimen?>
                log!!.debug("find all paged successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("find all failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("Find all paged failed. ", re)
            throw re
        }
    }

    //Select distinct path from ICImage im where im.path is not null order by im.path
    fun findImagesByPath(path: String): MutableList<ICImage?>? {
        log!!.debug("finding images by path $path")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<ICImage?>? = null
            try { //System.out.println("path is " + path);
//this works
                var sql = ""
                sql = if (path.contains("\\")) {
                    "From ICImage im where im.path='$path\\' order by imageId"
                } else {
                    "From ICImage im where im.path='$path' order by imageId"
                }
                //String sql = "From ICImage im where im.path='"+path+"\\\' order by imageId";
                val query = session.createQuery(sql)
                results = query!!.list() as MutableList<ICImage?>
                //log.debug("found images, result size: " + results.size());
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("find images failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("Find images failed. ", re)
            throw re
        }
    }// put blank at top of list.

    //get all image paths (folder name / date imaged) available
    val distinctPaths: Array<String?>?
        get() {
            val collections = ArrayList<String?>()
            collections.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct im.path from ICImage im order by im.imageId"
                loadStringsBySQL(collections, sql)
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

    private fun loadStringsBySQL(collections: ArrayList<String?>, sql: String?): Array<String?>? {
        val session = HibernateUtil.sessionFactory!!.currentSession
        try {
            session!!.beginTransaction()
            val q = session.createQuery(sql)
            val i = q!!.iterate()
            while (i!!.hasNext()) {
                val value = (i.next() as String?)!!
                // add, only if value isn't the "" put at top of list above.
                if (value != "") {
                    collections.add(value.trim { it <= ' ' })
                }
            }
            session.transaction.commit()
        } catch (e: HibernateException) {
            session!!.transaction.rollback()
            log!!.error(e.message)
        }
        try {
            session!!.close()
        } catch (e: SessionException) {
        }
        return collections.toArray(arrayOf())
    }

    fun findSpecimenCount(): String {
        try {
            return findSpecimenCountThrows()
        } catch (e: ConnectionException) {
            log!!.error(e.message, e)
        }
        return ""
    }

    @Throws(ConnectionException::class)
    fun findSpecimenCountThrows(): String {
        val result = StringBuffer()
        return try {
            val sql = "Select count(*), workFlowStatus from Specimen group by workFlowStatus "
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                result.append("Specimen records: \n")
                session!!.beginTransaction()
                val results = session.createQuery(sql).list().iterator()
                while (results.hasNext()) {
                    val row = results.next() as Array<Any?>?
                    val count = (row!![0] as Long?)!!
                    val status = (row[1] as String?)!!
                    result.append(" $status=$count\n")
                }
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
            result.toString()
        } catch (re: RuntimeException) {
            log!!.error(re)
            throw ConnectionException(re.message, re)
        }
    }

    /**
     * Obtain counts of the number of specimens needing verbatim transcription by genus and species.
     *
     * @return
     */
    fun countSpecimensForVerbatim(): MutableList<GenusSpeciesCount?> {
        val result: ArrayList<GenusSpeciesCount?> = ArrayList<GenusSpeciesCount?>()
        try {
            val sql = "Select count(S), genus, specificEpithet from Specimen S where S.workFlowStatus = 'Taxon Entered' group by S.genus, S.specificEpithet "
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val results = session.createQuery(sql).list().iterator()
                while (results.hasNext()) {
                    val row = results.next() as Array<Any?>?
                    val count = row!![0].toString().toLong()
                    val genus = (row[1] as String?)!!
                    val specificEpithet = (row[2] as String?)!!
                    result.add(GenusSpeciesCount(count, genus, specificEpithet))
                }
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error(re)
        }
        return result
    }

    /**
     * Get counts of distinct values of verbatim data that has not yet been classified.
     *
     * @return List of counts and distinct verbatim field values.
     */
    fun countDistinctVerbatimValues(): MutableList<VerbatimCount?> {
        val result: ArrayList<VerbatimCount?> = ArrayList<VerbatimCount?>()
        try {
            val sql = ("Select count(S), S.verbatimLocality, S.dateNos, S.verbatimCollector, S.verbatimCollection, "
                    + "S.verbatimNumbers, S.verbatimUnclassifiedText "
                    + "from Specimen S "
                    + "where S.workFlowStatus = 'Verbatim Entered' "
                    + "group by S.verbatimLocality, S.dateNos, S.verbatimCollector, S.verbatimCollection, "
                    + "S.verbatimNumbers, S.verbatimUnclassifiedText ")
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val results = session.createQuery(sql).list().iterator()
                while (results.hasNext()) {
                    val row = results.next() as Array<Any?>?
                    val count = row!![0].toString().toLong()
                    val verbatimLocality = (row[1] as String?)!!
                    val dateNos = (row[2] as String?)!!
                    val verbatimCollector = (row[3] as String?)!!
                    val verbatimCollection = (row[4] as String?)!!
                    val verbatimNumbers = (row[5] as String?)!!
                    val verbatimUnclassifiedText = (row[6] as String?)!!
                    result.add(VerbatimCount(count.toInt(), verbatimLocality, dateNos, verbatimCollector, verbatimCollection, verbatimNumbers, verbatimUnclassifiedText))
                }
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error(re)
        }
        return result
    }

    fun findForVerbatim(genus: String?, specificEpithet: String?, workflowStatus: String?): MutableList<Specimen?>? {
        log!!.debug("finding Specimen instances for verbatim capture")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            var results: MutableList<Specimen?>? = null
            try {
                session!!.beginTransaction()
                val query = session.createQuery("From Specimen as s where s.genus = ? and s.specificEpithet = ? and s.workFlowStatus = ? ")
                query!!.setParameter(0, genus)
                query.setParameter(1, specificEpithet)
                query.setParameter(2, workflowStatus)
                results = query.list() as MutableList<Specimen?>
                log!!.debug("find query successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error("find by example failed", e)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("find by example failed", re)
            throw re
        }
    }

    fun findTaxaFromVerbatim(verbatim: VerbatimCount): MutableList<CountValue?> {
        log!!.debug("finding counts of taxa for verbatim values")
        val result: MutableList<CountValue?> = ArrayList<CountValue?>()
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val query = session.createQuery("Select count(s), s.genus, s.specificEpithet "
                        + "From Specimen as s "
                        + "where s.verbatimLocality = ? and s.dateNos = ? "
                        + "   and s.verbatimCollector = ? and s.verbatimCollection = ? "
                        + "   and s.verbatimNumbers = ? and s.verbatimUnclassifiedText = ? "
                )
                query!!.setParameter(0, verbatim.getVerbatimLocality())
                query.setParameter(1, verbatim.getVerbatimDate())
                query.setParameter(2, verbatim.getVerbatimCollector())
                query.setParameter(3, verbatim.getVerbatimCollection())
                query.setParameter(4, verbatim.getVerbatimNumbers())
                query.setParameter(5, verbatim.getVerbatimUnclassfiedText())
                val i = query.list().iterator()
                while (i.hasNext()) {
                    val row = i.next() as Array<Any?>?
                    val count = row!![0].toString().toLong()
                    val genus = (row[1] as String?)!!
                    val specificEpithet = (row[2] as String?)!!
                    val taxon = StringBuffer().append(genus).append(" ").append(specificEpithet)
                    result.add(CountValue(count.toInt(), taxon.toString().trim { it <= ' ' }))
                }
                log!!.debug("count query successful, result size: " + result.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error("find for verbatim failed", e)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
            result
        } catch (re: RuntimeException) {
            log!!.error("find for verbatim failed", re)
            throw re
        }
    }

    /**
     * Find specimen records that are currently in state verbatim captured and which have
     * the provided values for verbatim fields.
     *
     * @param verbatim
     * @return
     */
    fun findSpecimensFromVerbatim(verbatim: VerbatimCount): MutableList<Specimen?>? {
        log!!.debug("finding specimens from verbatim values")
        var result: MutableList<Specimen?> = ArrayList<Specimen?>()
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val query = session.createQuery("From Specimen as s "
                        + "where s.verbatimLocality = ? and s.dateNos = ? "
                        + "   and s.verbatimCollector = ? and s.verbatimCollection = ? "
                        + "   and s.verbatimNumbers = ? and s.verbatimUnclassifiedText = ? "
                        + "   and s.workFlowStatus = '" + WorkFlowStatus.STAGE_VERBATIM + "' "
                )
                query!!.setParameter(0, verbatim.getVerbatimLocality())
                query.setParameter(1, verbatim.getVerbatimDate())
                query.setParameter(2, verbatim.getVerbatimCollector())
                query.setParameter(3, verbatim.getVerbatimCollection())
                query.setParameter(4, verbatim.getVerbatimNumbers())
                query.setParameter(5, verbatim.getVerbatimUnclassfiedText())
                result = query.list()
                log!!.debug("specimen query successful, result size: " + result.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error("find specimens for verbatim failed", e)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
            result
        } catch (re: RuntimeException) {
            log!!.error("find specimens for verbatim failed", re)
            throw re
        }
    }

    fun getFieldSize(fieldName: String?): Int {
        //		Specimen s = new Specimen();
// ClassMetadata specimenMetadata = HibernateUtil.sessionFactory.getClassMetadata(Specimen.class);
// String[] p = specimenMetadata.getPropertyNames();
// for (int i=0; i < p.length; i++ ) {
// 	System.out.println(p[i]);
// 	specimenMetadata.getPropertyValue(p[i], "length", EntityMode.MAP);
// }
//		Object[] propertyValues = specimenMetadata.getPropertyValues(s, EntityMode.POJO);
//		String[] propertyNames = specimenMetadata.getPropertyNames();
//		Type[] propertyTypes = specimenMetadata.getPropertyTypes();
//
//		// get a Map of all properties which are not collections or associations
//		Map namedValues = new HashMap();
//		for ( int i=0; i<propertyNames.length; i++ ) {
//		        System.out.println( propertyNames[i] + " : " + propertyValues[i] );
//
//		}
        return 0
    }

    // put blank at top of list.
    val distinctCountries: Array<String?>?
        get() {
            val collections = ArrayList<String?>()
            collections.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct country from Specimen spe where spe.country is not null order by spe.country  "
                loadStringsBySQL(collections, sql)
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

    fun deleteSpecimenByBarcode(barcode: String?): Int {
        log!!.debug("Deleting record with barcode $barcode")
        val specimens: MutableList<Specimen?>? = findByBarcode(barcode)
        if (specimens!!.size == 0) {
            return 0 //specimen not found
        } else if (specimens.size >= 1) {
            val specimen: Specimen? = specimens[0]
            //this does not work - need to do it manually as below!!
//this.delete(specimen);
            val specimenId: Long = specimen.getSpecimenId()
            try {
                val session = HibernateUtil.sessionFactory!!.currentSession
                session!!.beginTransaction()
                try {
                    var query = session.createQuery("delete from Tracking where Tracking.specimen =?")
                    query!!.setParameter(0, specimenId)
                    query.executeUpdate()
                    query = session.createQuery("delete from LatLong where LatLong.specimenId =?")
                    query.setParameter(0, specimenId)
                    query.executeUpdate()
                    query = session.createQuery("delete from ICImage where ICImage.specimen =?")
                    query.setParameter(0, specimenId)
                    query.executeUpdate()
                    query = session.createQuery("delete from SpecimenPart where SpecimenPart.specimenId =?")
                    query.setParameter(0, specimenId)
                    query.executeUpdate()
                    query = session.createQuery("delete from Specimen where Specimen.id =?")
                    query.setParameter(0, specimenId)
                    query.executeUpdate()
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session.transaction.rollback()
                    log!!.error("DeleteByBarcode failed Hibernate Exception", e)
                } catch (e: Exception) {
                    session.transaction.rollback()
                    log!!.error("findByBarcode failed general Exception", e)
                }
                try {
                    session.close()
                } catch (e: SessionException) {
                }
            } catch (re: RuntimeException) {
                log!!.error("findByBarcode failed.  ", re)
                throw re
            }
        }
        val specimensAfterDelete: MutableList<Specimen?>? = findByBarcode(barcode)
        return if (specimensAfterDelete!!.size == 0) {
            1 //success
        } else 2
        //delete failed for unknown reason
    }

    // put blank at top of list.
    val distinctCollections: Array<String?>?
        get() {
            val collections = ArrayList<String?>()
            collections.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct collection from Specimen spe where spe.collection is not null order by spe.collection  "
                loadStringsBySQL(collections, sql)
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }// add, only if value isn't the "" put at top of list above.//String sql = "Select distinct identifiedby from Specimen";

    // put blank at top of list.
    val distinctDeterminers: Array<String?>?
        get() {
            val collections = ArrayList<String?>()
            collections.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct identifiedBy from Specimen spe where spe.identifiedBy is not null order by spe.identifiedBy  "
                //String sql = "Select distinct identifiedby from Specimen";
                val session = HibernateUtil.sessionFactory!!.currentSession
                try {
                    session!!.beginTransaction()
                    val q = session.createQuery(sql)
                    val i = q!!.iterate()
                    while (i!!.hasNext()) {
                        val value = (i.next() as String?)!!
                        // add, only if value isn't the "" put at top of list above.
                        if (value != "") {
                            collections.add(value.trim { it <= ' ' })
                        }
                    }
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    e.printStackTrace()
                    session!!.transaction.rollback()
                    log!!.error(e.message)
                }
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
                collections.toArray(arrayOf())
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }// add, only if value isn't the "" put at top of list above.//String sql = "Select distinct identifiedby from Specimen";

    // put blank at top of list.
    val distinctPrimaryDivisions: Array<String?>?
        get() {
            val collections = ArrayList<String?>()
            collections.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct primaryDivison from Specimen spe where spe.primaryDivison is not null order by spe.primaryDivison  "
                //String sql = "Select distinct identifiedby from Specimen";
                val session = HibernateUtil.sessionFactory!!.currentSession
                try {
                    session!!.beginTransaction()
                    val q = session.createQuery(sql)
                    val i = q!!.iterate()
                    while (i!!.hasNext()) {
                        val value = (i.next() as String?)!!
                        // add, only if value isn't the "" put at top of list above.
                        if (value != "") {
                            collections.add(value.trim { it <= ' ' })
                        }
                    }
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    e.printStackTrace()
                    session!!.transaction.rollback()
                    log!!.error(e.message)
                }
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
                collections.toArray(arrayOf())
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

    // put blank at top of list.
    val distinctQuestions: Array<String?>?
        get() {
            val collections = ArrayList<String?>()
            collections.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct questions from Specimen spe where spe.questions is not null order by spe.questions  "
                loadStringsBySQL(collections, sql)
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

    override fun findByExampleLike(instance: Specimen): MutableList<Specimen?>? {
        return this.findByExampleLike(instance, 0, 0)
    }

    /** Find specimens with values matching those found in an example specimen instance, including links out
     * to related entities.  Like matching is enabled, so strings containing '%' will generate like where
     * criteria with the % as a wild card.  Matches are case insensitive.
     *
     * @param instance
     * @return
     */
    override fun findByExampleLike(instance: Specimen, maxResults: Int, offset: Int): MutableList<Specimen?>? {
        log!!.debug("finding Specimen instance by example with trackings, collectors, and images and like criteria")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<Specimen?>? = null
            try {
                var criteria: Criteria = session.createCriteria(Specimen::class.java)
                instance.setFlagAncilaryAlsoInMCZbase(null)
                instance.setFlagInBulkloader(null)
                instance.setFlagInMCZbase(null)
                val example: Example = Example.create(instance).enableLike().ignoreCase()
                // Note: Criteria in example instance can be excluded here, or by setting their
// values to null.  See the invocation of Specimen.clearDefaults in
// SearchDialog.getJButtonSearch()'s actionPerformed method.
//example.excludeProperty("flagInBulkloader");
                criteria.add(example)
                if (instance.getTrackings() != null && instance.getTrackings().size > 0) {
                    criteria.createCriteria("trackings", Criteria.INNER_JOIN).add(Example.create(instance.getTrackings().toTypedArray().get(0)).enableLike())
                }
                if (instance.getICImages() != null && instance.getICImages().size > 0) {
                    criteria.createCriteria("ICImages", Criteria.INNER_JOIN).add(Example.create(instance.getICImages().toTypedArray().get(0)).enableLike().ignoreCase())
                }
                if (instance.getCollectors() != null && instance.getCollectors().size > 0) {
                    criteria.createCriteria("collectors", Criteria.INNER_JOIN).add(Example.create(instance.getCollectors().toTypedArray().get(0)).enableLike().ignoreCase())
                }
                if (offset != 0) {
                    criteria.setFirstResult(offset)
                }
                if (maxResults != 0) {
                    criteria.setMaxResults(maxResults)
                }
                criteria.setProjection(Projections.id())
                val ids = criteria.list()
                if (ids!!.size > 0) {
                    criteria = session.createCriteria(Specimen::class.java)
                            .add(Restrictions.`in`("id", ids))
                            .setFetchMode("trackings", FetchMode.JOIN)
                            .setFetchMode("ICImages", FetchMode.JOIN)
                            .setFetchMode("collectors", FetchMode.JOIN)
                            .setFetchMode("LatLong", FetchMode.JOIN)
                            .setFetchMode("numbers", FetchMode.JOIN)
                            .setFetchMode("parts", FetchMode.JOIN)
                            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    results = criteria.list() as MutableList<Specimen?>
                } else {
                    results = ArrayList<Specimen?>()
                }
                log!!.debug("find by example like successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("find by example like failed", e)
            }
            if (results != null) {
                for (i in results.indices) { /* try {
                        log.debug("Parts: " + results.get(i).getSpecimenParts().size());
                        log.debug("Parts: " + ((SpecimenPart) results.get(i).getSpecimenParts().toArray()[0]).getPartAttributeValuesConcat());
                        log.debug("Part Attribute: " + ((SpecimenPartAttribute) ((SpecimenPart) results.get(i).getSpecimenParts().toArray()[0]).getAttributeCollection().toArray()[0]).getSpecimenPartAttributeId());
                    } catch (Exception e) {
                        log.debug(e.getMessage());
                    }*/
                }
            } else {
                results = ArrayList<Specimen?>()
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("find by example like failed", re)
            throw re
        }
    }

    override fun findByExample(instance: Specimen): MutableList<Specimen?>? {
        return this.findByExample(instance, 0, 0)
    }

    /**
     * Find Specimen records based on an example specimen, don't use if you are only searching by barcode.
     *
     * @param instance Specimen instance to use as a pattern for the search
     * @return list of Specimens matching instance
     *
     * @see SpecimenLifeCycle.findByBarcode
     */
    override fun findByExample(instance: Specimen, maxResults: Int, offset: Int): MutableList<Specimen?>? {
        log!!.debug("finding Specimen instance by example")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<Specimen?>? = null
            try {
                val criteria: Criteria = session.createCriteria(Specimen::class.java)
                criteria.add(Example.create(instance))
                if (instance.getTrackings() != null && instance.getTrackings().size > 0) {
                    criteria.createCriteria("trackings").add(Example.create(instance.getTrackings().toTypedArray().get(0)))
                }
                if (instance.getICImages() != null && instance.getICImages().size > 0) {
                    criteria.createCriteria("ICImages").add(Example.create(instance.getICImages().toTypedArray().get(0)))
                }
                if (offset != 0) {
                    criteria.setFirstResult(offset)
                }
                if (maxResults != 0) {
                    criteria.setMaxResults(maxResults)
                }
                results = criteria.list() as MutableList<Specimen?>
                log!!.debug("find by example successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("find by example failed", e)
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("find by example failed", re)
            throw re
        }
    }

    companion object {
        private val log = LogFactory.getLog(SpecimenLifeCycle::class.java)
        /**
         * Barcodes are assigned in order (from 50,000). Find barcode numbers that are missing
         * from the sequence of specimen records.
         *
         * @return a string array containing a list of barcodes that are missing from the sequence.
         */
        val missingBarcodes: Array<String?>?
            get() {
                val missing = ArrayList<String?>()
                return try {
                    val sql = "select " +
                            "        (cast(substr(a.barcode,-8) as decimal(8,0)) + 1) " +
                            "        from Specimen a " +
                            "        where " +
                            "        not exists " +
                            "        (" +
                            "            select 1 from Specimen b " +
                            "            where " +
                            "            cast(substr(b.barcode,-8) as decimal(8,0)) = (cast(substr(a.barcode,-8) as decimal(8,0)) + 1)" +
                            "        ) " +
                            "        and " +
                            "        cast(substr(a.barcode,-8) as decimal(8,0)) not in " +
                            "        ( " +
                            "          select max(cast(substr(c.barcode,-8) as decimal(8,0))) from Specimen c where cast(substr(a.barcode,-8) as decimal(8,0)) > 49999 " +
                            "        ) " +
                            "        order by 1"
                    val session = HibernateUtil.sessionFactory!!.currentSession
                    try {
                        session!!.beginTransaction()
                        val q = session.createQuery(sql)
                        val results = q!!.list()
                        val i = results!!.iterator()
                        while (i.hasNext()) {
                            val value: BigDecimal = i.next() as BigDecimal?
                            val builder: BarcodeBuilder = Singleton.getBarcodeBuilder()
                            missing.add(builder.makeFromNumber(value.toBigInteger().intValue()))
                            log!!.debug(value)
                        }
                        session.transaction.commit()
                    } catch (e: HibernateException) {
                        session!!.transaction.rollback()
                        log!!.error(e.message)
                    }
                    try {
                        session!!.close()
                    } catch (e: SessionException) {
                    }
                    missing.toArray(arrayOf())
                } catch (re: RuntimeException) {
                    log!!.error(re)
                    arrayOf()
                }
            }

        @JvmStatic
        fun main(args: Array<String>) {
            val s = SpecimenLifeCycle()
            s.getFieldSize("")
        }
    }
}
