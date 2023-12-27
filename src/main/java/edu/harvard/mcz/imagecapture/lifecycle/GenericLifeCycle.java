package edu.harvard.mcz.imagecapture.lifecycle;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.query.Specification;
import jakarta.persistence.criteria.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.query.Query;
import org.slf4j.Logger;

import java.util.*;

public abstract class GenericLifeCycle<T> {

    private final Class<T> tCLass;
    private final String idProperty;
    protected Logger log;
    boolean anon = false;

    public GenericLifeCycle(Class<T> tClass, Logger log) {
        this(tClass, log, "id");
    }

    public GenericLifeCycle(Class<T> tClass, Logger log, String idProperty) {
        this.tCLass = tClass;
        this.log = log;
        this.idProperty = idProperty;
    }

    protected Session getSession() {
        return HibernateUtil.getSessionFactory(anon).getCurrentSession();
    }

    public void goAnon() {
        this.anon = true;
    }

    public T findOneBy(String propertyPath, Object value) {
        log.debug("getting one " + this.tCLass.toGenericString() + " by " + propertyPath);
        try {
            T instance = null;
            Session session = this.getSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = (CriteriaBuilder) session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(this.tCLass);
                Root<T> root = cr.from(this.tCLass);
                cr = cr.where(cb.equal(root.get(propertyPath), value));
                instance = (T) session.createQuery(cr).getSingleResult();
                if (instance == null) {
                    log.debug("get successful, no instance found");
                } else {
                    log.debug("get successful, instance found");
                }
                session.getTransaction().commit();
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

    abstract public List<T> findByIds(List<Long> ids);

    public List<T> findBy(Map<String, Object> propertyValueMap, int maxResults, int offset, boolean like) {
        log.debug("finding " + this.tCLass.toGenericString() + " instance by hashmap: " + propertyValueMap.toString());
        try {
            Session session = this.getSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = (CriteriaBuilder) session.getCriteriaBuilder();
                CriteriaQuery<Long> cr = cb.createQuery(Long.class);
                Root<T> root = cr.from(this.tCLass);
                List<Predicate> propertyValueRelations = new ArrayList<>();
                for (Map.Entry<String, Object> entry : propertyValueMap.entrySet()) {
                    Expression<String> propertyToMatch = null;
                    // handle paths/joins etc.
                    String[] splitKey = entry.getKey().split("\\.");
                    if (splitKey.length > 1) {
                        Join<Object, Object> currentRoot = root.join(splitKey[0]);
                        for (int i = 1; i < splitKey.length - 1; i++) {
                            currentRoot = currentRoot.join(splitKey[i]);
                        }
                        propertyToMatch = currentRoot.get(splitKey[splitKey.length - 1]);
                    } else {
                        propertyToMatch = root.get(entry.getKey());
                    }

                    Predicate p = null;
                    if (entry.getValue() instanceof Specification) {
                        p = ((Specification<T, Long>) entry.getValue()).toPredicate(root, cr, cb);
                    } else if (like && entry.getValue() instanceof String) {
                        p = cb.like(propertyToMatch, (String) entry.getValue());
                    } else {
                        Object value = entry.getValue();
                        if (value instanceof Boolean || value instanceof String || value instanceof Date || value instanceof java.lang.Number) {
                            p = cb.equal(propertyToMatch, entry.getValue());
                        } else {
                            log.warn("Will not handle property: " + entry.getKey() + " with value type " + " " + value.getClass().toGenericString());
                        }
                    }
                    if (p != null) {
                        propertyValueRelations.add(p);
                    }
                }
                cr.where(cb.and(propertyValueRelations.toArray(new Predicate[propertyValueRelations.size()])));
                cr.select(root.get(this.idProperty));
                Query q = session.createQuery(cr);
                if (maxResults != 0) {
                    q = q.setMaxResults(maxResults);
                }
                if (offset != 0) {
                    q = q.setFirstResult(offset);
                }
                // then, do join all
                List<Long> ids = q.list();
                log.debug("find by example successful, result size: " + ids.size());
                session.getTransaction().commit();
                return this.findByIds(ids);

            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            }
            return new ArrayList<T>();
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
        // from https://stackoverflow.com/questions/43499887/hibernate-5-2-get-natural-id-properties-from-metamodel
        MetamodelImplementor metamodel = (MetamodelImplementor) this.getSession().getMetamodel();
        ClassMetadata classMetadata = (ClassMetadata) metamodel.entityPersister(instance.getClass().getName());
        String[] propertyNames = classMetadata.getPropertyNames();
        return Arrays.asList(propertyNames);
    }

    public List<T> findByExample(T instance, int maxResults, int offset) {
        log.debug("finding " + tCLass.toGenericString() + " instance by example");
        try {
            List<T> results = null;
            Session session = this.getSession();
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
        Session session = this.getSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery(sql);
            collections.addAll(q.list());
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
