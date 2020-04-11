package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException
import java.util.*

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class ICImage.
 * Refactored from generated EJB home class to use with local singleton
 * HibernateUtil for sessions instead of EJB. Renamed from Home to LifeCycle to
 * prevent overwriting by Hibernate Tools
 *
 * @see ICImage
 */
class ICImageLifeCycle : GenericLifeCycle<ICImage?>(ICImage::class.java, log) {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: ICImage?) {
        log!!.debug("persisting ICImage instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.persist(transientInstance)
                session.transaction.commit()
                log!!.debug("persist successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Save to Image table failed. " +
                        e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error("persist failed", re)
            throw re
        }
    }

    @Throws(SaveFailedException::class)
    fun attachDirty(instance: ICImage?) {
        log!!.debug("attaching dirty ICImage instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.saveOrUpdate(instance)
                session.transaction.commit()
                log!!.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Save to image table failed. " +
                        e.message)
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

    @Throws(SaveFailedException::class)
    fun attachClean(instance: ICImage?) {
        log!!.debug("attaching clean ICImage instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.lock(instance, LockMode.NONE)
                session.transaction.commit()
                log!!.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Save to image table failed. " +
                        e.message)
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

    @Throws(SaveFailedException::class)
    fun delete(persistentInstance: ICImage?) {
        log!!.debug("deleting ICImage instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.delete(persistentInstance)
                session.transaction.commit()
                log!!.debug("delete successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Delete from image table failed. " +
                        e.message)
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

    @Throws(SaveFailedException::class)
    fun merge(detachedInstance: ICImage?): ICImage? {
        log!!.debug("merging ICImage instance")
        return try {
            var result: ICImage? = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as ICImage
                session.transaction.commit()
                log!!.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Save to image table failed. " +
                        e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            result
        } catch (re: RuntimeException) {
            log!!.error("merge failed", re)
            throw re
        }
    }

    fun findById(id: Long?): ICImage? {
        log!!.debug("getting ICImage instance with id: $id")
        return try {
            var instance: ICImage? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session["edu.harvard.mcz.imagecapture.entity.ICImage", id] as ICImage
                session.transaction.commit()
                if (instance == null) {
                    log!!.debug("get successful, no instance found")
                } else {
                    log!!.debug("get successful, instance found")
                }
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
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

    fun findOneBySpecimen(specimen: Specimen): ICImage? {
        return super.findOneBy("SPECIMENID", specimen.getSpecimenId())
    }

    fun findOneByPath(path: String?): ICImage? {
        return super.findOneBy("path", path)
    }

    fun findBySpecimen(specimen: Specimen): MutableList<ICImage?>? {
        return super.findBy("SPECIMENID", specimen.getSpecimenId())
    }

    fun findByPath(path: String?): MutableList<ICImage?>? {
        return super.findBy("path", path)
    }

    /**
     * @return list of all image files sorted by filename
     */
    fun findAll(): MutableList<ICImage?>? {
        log!!.debug("finding all Images")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<ICImage?>? = null
            try {
                results = session
                        .createQuery("From ICImage i order by i.filename")
                        .list() as MutableList<ICImage?>
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
            log!!.error("find all failed", re)
            throw re
        }
    }

    /**
     * Find images with a particular path.
     *
     * @return list of all image files with a particular path.
     */
    fun findAllInDir(path: String?): MutableList<ICImage?>? {
        log!!.debug("finding all Images with path $path")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<ICImage?>? = null
            try {
                results = session
                        .createQuery("From ICImage i where path = '" + path +
                                "' order by i.filename")
                        .list() as MutableList<ICImage?>
                log!!.debug("find in directory successful, result size: " +
                        results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("find in dir failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("find in dir failed", re)
            throw re
        }
    }// put blank at top of list.

    /**
     * Obtain an array of distinct values of paths suitable for populating a
     * picklist.
     * Example usage:
     * `
     * ICImageLifeCycle ils = new ICImageLifeCycle();
     * JComboBox jComboPaths = new JComboBox(ils.getDistinctPaths());
     * jComboPaths.setEditable(true);
     * TableColumn pathColumn =
     * jTableImages.getColumnModel().getColumn(ICImageListTableModel.COL_PATH);
     * pathColumn.setCellEditor(new DefaultCellEditor(jComboPaths));
    ` *
     *
     * @return a string array containing the distinct values of IMAGE path with a
     * "" as the
     * first item in the array.
     */
    val distinctPaths: Array<String?>?
        get() {
            val types = ArrayList<String?>()
            types.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct path from ICImage im where im.path is not null order by im.path  "
                val session = HibernateUtil.sessionFactory!!.currentSession
                try {
                    session!!.beginTransaction()
                    val q = session.createQuery(sql)
                    val i = q!!.iterate()
                    while (i!!.hasNext()) {
                        val value = (i.next() as String?)!!
                        types.add(value)
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
                types.toArray(arrayOf())
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

    companion object {
        private val log = LogFactory.getLog(ICImageLifeCycle::class.java)
        fun findMismatchedImages(): MutableList<ICImage?>? {
            log!!.debug("finding Images with barcode missmatches ")
            return try {
                val session = HibernateUtil.sessionFactory!!.currentSession
                session!!.beginTransaction()
                var results: MutableList<ICImage?>? = null
                try {
                    results = session
                            .createQuery(
                                    "From ICImage i where (rawBarcode <> rawExifBarcode or (rawBarcode is null) or (rawExifBarcode is null)) order by i.filename")
                            .list() as MutableList<ICImage?>
                    log.debug("find missmatches successful, result size: " +
                            results!!.size)
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session.transaction.rollback()
                    log.error("find missmatches failed", e)
                }
                try {
                    session.close()
                } catch (e: SessionException) {
                }
                results
            } catch (re: RuntimeException) {
                log.error("find missmatches failed", re)
                throw re
            }
        }

        fun findDrawerImages(aDrawerNumber: String): MutableList<ICImage?>? {
            log!!.debug("finding Images for drawer $aDrawerNumber")
            return try {
                val session = HibernateUtil.sessionFactory!!.currentSession
                session!!.beginTransaction()
                var results: MutableList<ICImage?>? = null
                try {
                    results = session
                            .createQuery(
                                    "From ICImage i where specimen is null and drawerNumber = '" +
                                            aDrawerNumber.trim { it <= ' ' } + "' order by i.filename")
                            .list() as MutableList<ICImage?>
                    log.debug("find all successful, result size: " + results!!.size)
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session.transaction.rollback()
                    log.error("find all failed", e)
                }
                try {
                    session.close()
                } catch (e: SessionException) {
                }
                results
            } catch (re: RuntimeException) {
                log.error("find all failed", re)
                throw re
            }
        }

        fun findNotDrawerNoTemplateImages(): MutableList<ICImage?>? {
            log!!.debug("finding Images for template " +
                    PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS)
            return try {
                val session = HibernateUtil.sessionFactory!!.currentSession
                session!!.beginTransaction()
                var results: MutableList<ICImage?>? = null
                try {
                    results = session
                            .createQuery(
                                    "From ICImage i where specimen is not null and templateId = '" +
                                            PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS.trim({ it <= ' ' }) +
                                            "' order by i.filename")
                            .list() as MutableList<ICImage?>
                    log.debug(
                            "find all whole image only records with specimens successful, result size: " +
                                    results!!.size)
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session.transaction.rollback()
                    log.error("find all whole image only records failed", e)
                }
                try {
                    session.close()
                } catch (e: SessionException) {
                }
                results
            } catch (re: RuntimeException) {
                log.error("find all whole image only records failed", re)
                throw re
            }
        }
    }
}
