package edu.harvard.mcz.imagecapture.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.sql.Restriction;

public interface RestrictiveSpec<T> {
    Restriction toRestriction(Root<T> queryRoot, CriteriaQuery<T> query, CriteriaBuilder cb);
}
