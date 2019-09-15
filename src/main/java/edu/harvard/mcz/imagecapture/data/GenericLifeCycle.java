package edu.harvard.mcz.imagecapture.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
//import org.hibernate.metamodel.model.domain.spi.EntityTypeDescriptor;
//import org.hibernate.metamodel.model.domain.spi.NonIdPersistentAttribute;
//import org.hibernate.metamodel.spi.MetamodelImplementor;

import javax.persistence.AttributeNode;
import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class GenericLifeCycle<T> {

    protected Log log;

    private final Class<T> tCLass;


    public GenericLifeCycle(Class<T> tClass, Log log) {
        this.tCLass = tClass;
        this.log = log;
    }

    public T findOneBy(String propertyPath, Object value) {
        log.debug("getting one " + this.tCLass.getClass().toGenericString() + " by " + propertyPath);
        try {
            T instance = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(this.tCLass.getClass());
                Root<T> root = cr.from(this.tCLass.getClass());
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
        log.debug("getting " + this.tCLass.getClass().toGenericString() + " by " + propertyPath);
        try {
            List<T> resultSet = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(this.tCLass.getClass());
                Root<T> root = cr.from(this.tCLass.getClass());
                cr = cr.where(cb.equal(root.get(propertyPath), value));
                resultSet = session.createQuery(cr).list();
                log.debug("Found " + resultSet.size() + " results.");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            }
            return resultSet;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<T> findBy(Map<String, Object> propertyValueMap) {
        log.debug("finding " + this.tCLass.toGenericString() + " instance by hashmap");
        try {
            List<T> results = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(this.tCLass);
                Root<T> root = cr.from(this.tCLass);
                cr.select(root);
                List<Predicate> propertyValueRelations = new ArrayList<>();
                for (Map.Entry<String, Object> entry : propertyValueMap.entrySet()) {
                    propertyValueRelations.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
                }
                cr.where(cb.and(propertyValueRelations.toArray(new Predicate[propertyValueRelations.size()])));
                results = session.createQuery(cr).list();
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
     *
     * This is the method for Hibernate < 5.0.0
     *
     * @param instance the entity to get the attributes of
     * @param session the hibernate session
     * @return List[String] all the attributes
     */
    private List<String> getEntityAttributes(T instance, Session session) {
        // untested. other possible implementations: https://stackoverflow.com/questions/19418500/get-table-column-names-in-hibernate

        ClassMetadata classMetadata = HibernateUtil.getSessionFactory().getClassMetadata(instance.getClass());
        String[] propertyNames = classMetadata.getPropertyNames();
        return Arrays.asList(propertyNames);
    }

    public List<T> findByExample(T instance) {
        log.debug("finding " + tCLass.toGenericString() + " instance by example");
        try {
            List<T> results = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(instance.getClass());
                Root<T> root = cr.from(tCLass);
                cr.select(root);
                // loop properties & find the ones which are not null
                List<String> persistentAttributes = this.getEntityAttributes(instance, session);
                 log.debug("found entity attributes: " + persistentAttributes.toString());
                int attributesSize = persistentAttributes.size();
                List<Predicate> propertyValueRelations = new ArrayList<>();
                for (int i = 0; i < attributesSize; i++) {
                // TODO: refactor to create a map to be used on findBy()
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
                        if (propertyValue != null) {
                            // if not, add a where
                            // TODO: correct for property vs. column name
                            propertyValueRelations.add(cb.equal(root.get(attrName), propertyValue));
                        }
                    }

                cr.where(cb.and(propertyValueRelations.toArray(new Predicate[propertyValueRelations.size()])));
                results = session.createQuery(cr).list();
                session.getTransaction().commit();
                log.debug("find by example successful, result size: " + results.size());
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            }
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<T> findByExampleLike(T instance) {
        log.debug("finding " + instance.getClass().toGenericString() + " instance by example like");
        try {
            List<T> results = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(instance.getClass());
                Root<T> root = cr.from(instance.getClass());
                cr.select(root);
                // loop properties & find the ones which are not null
                List<String> persistentAttributes = this.getEntityAttributes(instance, session);
                log.debug("found entity attributes: " + persistentAttributes.toString());
                int attributesSize = persistentAttributes.size();
                List<Predicate> propertyValueRelations = new ArrayList<>();
                for (int i = 0; i < attributesSize; i++) {
                    String attrName = persistentAttributes.get(i);
                    // check if property is null
                    java.lang.reflect.Method getter;
                    Object propertyValue = null;
                    log.debug("Got attrName: " + attrName);
                    try {
                        getter = instance.getClass().getMethod("get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1));
                        propertyValue = getter.invoke(instance);
                    } catch (Exception e) {
                        log.warn("Failed to get getter method or value of '" + attrName + "' to findByExampleLike: " + e.getMessage());
                    }
                    // TODO: handle relations
                    if (propertyValue != null) {
                        // if not, add a where
                        // TODO: correct for property vs. column name
                        propertyValueRelations.add(cb.like(root.get(attrName), "%" + propertyValue + "%"));
                    }

                }
                cr.where(cb.and(propertyValueRelations.toArray(new Predicate[propertyValueRelations.size()])));
                results = session.createQuery(cr).list();
                session.getTransaction().commit();
                log.debug("find by example like successful, result size: " + results.size());
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            }
            return results;
        } catch (RuntimeException re) {
            log.error("find by example like failed", re);
            throw re;
        }
    }
}
