package edu.harvard.mcz.imagecapture.lifecycle;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
// import org.hibernate.metamodel.model.domain.spi.EntityTypeDescriptor;
// import org.hibernate.metamodel.model.domain.spi.NonIdPersistentAttribute;
// import org.hibernate.metamodel.spi.MetamodelImplementor;

public abstract class GenericLifeCycle<T> {

    private final Class<T> tCLass;
    protected Logger log;


    public GenericLifeCycle(Class<T> tClass, Logger log) {
        this.tCLass = tClass;
        this.log = log;
    }

    public T findOneBy(String propertyPath, Object value) {
        log.debug("getting one " + this.tCLass.toGenericString() + " by " + propertyPath);
        try {
            T instance = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(this.tCLass);
                Root<T> root = cr.from(this.tCLass);
                cr = cr.where(cb.equal(root.get(propertyPath), value));
                instance = (T) session.createQuery(cr).getSingleResult();
                if (instance == null) {
                    log.debug("get successful, no instance found");
                } else {
                    log.debug("get successful, instance found");
                }
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<T> findBy(String propertyPath, Object value) {
        return this.findBy(new HashMap<String, Object>() {{
            put(propertyPath, value);
        }}, 0, 0, false);
    }

    public List<T> findBy(Map<String, Object> propertyValueMap) {
        return this.findBy(propertyValueMap, 0, 0, false);
    }

    public List<T> findBy(Map<String, Object> propertyValueMap, int maxResults, int offset, boolean like) {
        log.debug("finding " + this.tCLass.toGenericString() + " instance by hashmap: " + propertyValueMap.toString());
        try {
            List<T> results = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                // TODO: handle paths/relationships
                session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(this.tCLass);
                Root<T> root = cr.from(this.tCLass);
                cr.select(root);
                List<Predicate> propertyValueRelations = new ArrayList<>();
                for (Map.Entry<String, Object> entry : propertyValueMap.entrySet()) {
                    Predicate p = null;
                    if (like && entry.getValue() instanceof String) {
                        p = cb.like(root.get(entry.getKey()), (String) entry.getValue());
                    } else {
                        Object value = entry.getValue();
                        if (value instanceof String || value instanceof Date || value instanceof java.lang.Number) {
                            p = cb.equal(root.get(entry.getKey()), entry.getValue());
                        } else {
                            // TODO: handle paths/relationships
                            log.warn("Will not handle property: " + entry.getKey() + " with value type " + " " + value.getClass().toGenericString());
                        }
                    }
                    if (p != null) {
                        propertyValueRelations.add(p);
                    }
                }
                cr.where(cb.and(propertyValueRelations.toArray(new Predicate[propertyValueRelations.size()])));
                Query q = session.createQuery(cr);
                if (maxResults != 0) {
                    q = q.setMaxResults(maxResults);
                }
                if (offset != 0) {
                    q = q.setFirstResult(offset);
                }
                results = q.getResultList();
                session.getTransaction().commit();
                log.debug("find by example successful, result size: " + results.size());
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            }
            return results;
        } catch (RuntimeException re) {
            log.error("find by hashmap failed", re);
            throw re;
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
     * <p>
     * This is the method for Hibernate < 5.0.0
     *
     * @param instance the entity to get the attributes of
     * @param session  the hibernate session
     * @return List[String] all the attributes
     */
    private List<String> getEntityAttributes(T instance, Session session) {
        // untested. other possible implementations: https://stackoverflow.com/questions/19418500/get-table-column-names-in-hibernate

        ClassMetadata classMetadata = HibernateUtil.getSessionFactory().getClassMetadata(instance.getClass());
        String[] propertyNames = classMetadata.getPropertyNames();
        return Arrays.asList(propertyNames);
    }

    public List<T> findByExample(T instance, int maxResults, int offset) {
        log.debug("finding " + tCLass.toGenericString() + " instance by example");
        try {
            List<T> results = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            // loop properties & find the ones which are not null
            List<String> persistentAttributes = this.getEntityAttributes(instance, session);
            log.debug("found entity attributes: " + persistentAttributes);
            int attributesSize = persistentAttributes.size();
            HashMap<String, Object> propertyValueRelations = new HashMap<String, Object>();
            for (int i = 0; i < attributesSize; i++) {
                // check if property is null
                java.lang.reflect.Method getter;
                Object propertyValue = null;
                String attrName = persistentAttributes.get(i);
                try {
                    getter = instance.getClass().getMethod("get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1));
                    propertyValue = getter.invoke(instance);
                } catch (Exception e) {
                    log.warn("Failed to get getter method or value of '" + attrName + "' to findByExample: " + e.getMessage());
                }
                // TODO: handle relations
                if (propertyValue != null && propertyValue != "" && !(propertyValue instanceof List && ((List) propertyValue).isEmpty())) {
                    // if not, add a where
                    // TODO: correct for property vs. column name
                    propertyValueRelations.put(attrName, propertyValue);
                }
            }

            results = this.findBy(propertyValueRelations, maxResults, offset, true);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<T> findByExample(T instance) {
        return this.findByExample(instance, 0, 0);
    }

    /**
     * @param instance
     * @return
     * @deprecated {use findByExample instead}
     */
    public List<T> findByExampleLike(T instance) {
        return this.findByExample(instance);
    }

    /**
     * @param instance
     * @return
     * @deprecated {use findByExample instead}
     */
    public List<T> findByExampleLike(T instance, int maxResults, int offset) {
        return this.findByExample(instance, maxResults, offset);
    }


    public String[] runQueryToGetStrings(ArrayList<String> collections, String sql, Logger log) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery(sql);
            Iterator i = q.getResultList().iterator();
            while (i.hasNext()) {
                String value = (String) i.next();
                // add, only if value isn't the "" put at top of list above.
                if (!value.equals("")) {
                    collections.add(value.trim());
                }
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            log.error(e.getMessage());
        }
        try {
            session.close();
        } catch (SessionException e) {
        }
        String[] result = collections.toArray(new String[]{});
        return result;
    }
}
