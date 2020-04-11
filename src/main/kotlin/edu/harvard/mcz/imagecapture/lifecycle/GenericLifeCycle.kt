package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.data.HibernateUtil
import org.apache.commons.logging.Log
import org.hibernate.HibernateException
import org.hibernate.Session
import org.hibernate.metadata.ClassMetadata
import java.lang.reflect.Method
import java.util.*
import javax.persistence.Query
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

// import org.hibernate.metamodel.model.domain.spi.EntityTypeDescriptor;
// import org.hibernate.metamodel.model.domain.spi.NonIdPersistentAttribute;
// import org.hibernate.metamodel.spi.MetamodelImplementor;
abstract class GenericLifeCycle<T>(private val tCLass: Class<T?>?, protected var log: Log?) {
    fun findOneBy(propertyPath: String?, value: Any?): T? {
        log!!.debug("getting one " + tCLass.javaClass.toGenericString() + " by " + propertyPath)
        return try {
            var instance: T? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val cb: CriteriaBuilder? = session.criteriaBuilder
                var cr: CriteriaQuery<*>? = cb.createQuery(tCLass.javaClass)
                val root: Root<T?> = cr!!.from(tCLass.javaClass)
                cr = cr.where(cb.equal(root.get(propertyPath), value))
                instance = session.createQuery<Any?>(cr).getSingleResult()
                if (instance == null) {
                    log!!.debug("get successful, no instance found")
                } else {
                    log!!.debug("get successful, instance found")
                }
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            }
            instance
        } catch (re: RuntimeException) {
            log!!.error("get failed", re)
            throw re
        }
    }

    fun findBy(propertyPath: String?, value: Any?): MutableList<T?>? {
        return this.findBy(object : HashMap<String?, Any?>() {
            init {
                put(propertyPath, value)
            }
        }, 0, 0, false)
    }

    @JvmOverloads
    fun findBy(propertyValueMap: MutableMap<String?, Any?>, maxResults: Int = 0, offset: Int = 0, like: Boolean = false): MutableList<T?>? {
        log!!.debug("finding " + tCLass!!.toGenericString() + " instance by hashmap: " + propertyValueMap.toString())
        return try {
            var results: MutableList<T?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            try { // TODO: handle paths/relationships
                session!!.beginTransaction()
                val cb: CriteriaBuilder? = session.criteriaBuilder
                val cr: CriteriaQuery<*> = cb.createQuery(tCLass)
                val root: Root<T?>? = cr.from(tCLass)
                cr.select(root)
                val propertyValueRelations: MutableList<Predicate?> = ArrayList()
                for ((key, value) in propertyValueMap) {
                    var p: Predicate? = null
                    if (like && value is String) {
                        p = cb.like(root.get(key), value as String?)
                    } else {
                        if (value is String || value is Date || value is Number) {
                            p = cb.equal(root.get(key), value)
                        } else { // TODO: handle paths/relationships
                            log!!.warn("Will not handle property: " + key + " with value type " + " " + value.javaClass.toGenericString())
                        }
                    }
                    if (p != null) {
                        propertyValueRelations.add(p)
                    }
                }
                cr.where(cb.and(*propertyValueRelations.toTypedArray()))
                var q: Query? = session.createQuery<Any?>(cr)
                if (maxResults != 0) {
                    q = q!!.setMaxResults(maxResults)
                }
                if (offset != 0) {
                    q = q!!.setFirstResult(offset)
                }
                results = q!!.resultList
                session.transaction.commit()
                log!!.debug("find by example successful, result size: " + results!!.size)
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("find by hashmap failed", re)
            throw re
        }
    }
    /**
     * Get the persistent attributes of an entity instance
     *
     * This is the method for Hibernate > 6.0.0
     *
     * @param instance the entity to get the attributes of
     * @param session the hibernate session
     * @return List[String] all the attributes
     */
/*private List<String> function getEntityAttributes(T instance, Session session) {
        // untested. other possible implementations: https://stackoverflow.com/questions/19418500/get-table-column-names-in-hibernate
        MetamodelImplementor metamodel = (MetamodelImplementor) session.getMetamodel();
        EntityTypeDescriptor descriptor = metamodel.getEntityDescriptor(instance.getClass());
        List<NonIdPersistentAttribute> persistentAttributes = descriptor.getPersistentAttributes();
        int attributesSize = persistentAttributes.size();
        List<String> attributes = new ArrayList<>();
        for (int i = 0; i < attributesSize; i++) {
            NonIdPersistentAttribute attribute =  persistentAttributes.get( i );
            if (attribute.isUpdateable()){
            attributes.add(attribute.getAttributeName());}
        }
        return attributes;
    }*/
    /**
     * Get the persistent attributes of an entity instance
     *
     *
     * This is the method for Hibernate < 5.0.0
     *
     * @param instance the entity to get the attributes of
     * @param session  the hibernate session
     * @return List[String] all the attributes
     */
    private fun getEntityAttributes(instance: T, session: Session?): MutableList<String?> { // untested. other possible implementations: https://stackoverflow.com/questions/19418500/get-table-column-names-in-hibernate
        val classMetadata: ClassMetadata? = HibernateUtil.sessionFactory!!.getClassMetadata(instance.javaClass)
        val propertyNames: Array<String?> = classMetadata.getPropertyNames()
        return Arrays.asList(*propertyNames)
    }

    open fun findByExample(instance: T, maxResults: Int, offset: Int): MutableList<T?>? {
        log!!.debug("finding " + tCLass!!.toGenericString() + " instance by example")
        return try {
            var results: MutableList<T?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            // loop properties & find the ones which are not null
            val persistentAttributes = getEntityAttributes(instance, session)
            log!!.debug("found entity attributes: $persistentAttributes")
            val attributesSize = persistentAttributes.size
            val propertyValueRelations = HashMap<String?, Any?>()
            for (i in 0 until attributesSize) { // check if property is null
                var getter: Method?
                var propertyValue: Any? = null
                val attrName = persistentAttributes[i]
                try {
                    getter = instance.javaClass.getMethod("get" + attrName!!.substring(0, 1).toUpperCase() + attrName.substring(1))
                    propertyValue = getter.invoke(instance)
                } catch (e: Exception) {
                    log!!.warn("Failed to get getter method or value of '" + attrName + "' to findByExample: " + e.message)
                }
                // TODO: handle relations
                if (propertyValue != null && propertyValue !== "" && !(propertyValue is MutableList<*> && (propertyValue as MutableList<*>?)!!.isEmpty())) { // if not, add a where
// TODO: correct for property vs. column name
                    propertyValueRelations[attrName] = propertyValue
                }
            }
            results = this.findBy(propertyValueRelations, maxResults, offset, true)
            log!!.debug("find by example successful, result size: " + results!!.size)
            results
        } catch (re: RuntimeException) {
            log!!.error("find by example failed", re)
            throw re
        }
    }

    open fun findByExample(instance: T): MutableList<T?>? {
        return this.findByExample(instance, 0, 0)
    }

    /**
     * @param instance
     * @return
     */
    @Deprecated("{use findByExample instead}")
    open fun findByExampleLike(instance: T): MutableList<T?>? {
        return this.findByExample(instance)
    }

    /**
     * @param instance
     * @return
     */
    @Deprecated("{use findByExample instead}")
    open fun findByExampleLike(instance: T, maxResults: Int, offset: Int): MutableList<T?>? {
        return this.findByExample(instance, maxResults, offset)
    }

}
