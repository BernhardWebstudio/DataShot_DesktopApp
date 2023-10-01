package edu.harvard.mcz.imagecapture.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface Specification<T> {
    Predicate toPredicate(Root<T> queryRoot, CriteriaQuery<T> query, CriteriaBuilder cb);
}