package edu.harvard.mcz.imagecapture.query;

import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.criterion.SimpleExpression;

import java.util.ArrayList;
import java.util.List;

public class DetachedQuery<T> {
    List<Expression> expressions;
    Class<T> rootClass;
    String rootAlias;

    List<String> aliasFor;
    List<String> alias;

    public DetachedQuery() {
        this.expressions = new ArrayList<Expression>();
        this.alias = new ArrayList<String>();
        this.aliasFor = new ArrayList<String>();
    }

    public DetachedQuery(Class<T> classT, String alias) {
        this();
        this.rootClass = classT;
        this.rootAlias = alias;
    }

    public static DetachedQuery forClass(Class<T> classT, String alias) {
        return new DetachedQuery(classT, alias);
    }

    public void add(Expression expression) {
        this.expressions.add(expression);
    }

    public void add(SimpleExpression expression) {
        this.expressions.add((Expression) expression);
    }

    public void createAlias(String path, String name) {
        aliasFor.add(path);
        alias.add(name);
    }

    public CriteriaQuery<T> toExecutableQuery(Session session) {
        CriteriaBuilder cb = (CriteriaBuilder) session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(this.rootClass);

        this.addToExecutableQuery((Root<T>) criteriaQuery.getRoots().toArray()[0], criteriaQuery, cb);
        return criteriaQuery;
//        criteriaQuery.
//        criteriaQuery.createAlias(this.rootClass, this.rootAlias);
    }

    public void addToExecutableQuery(Root<T> queryRoot, CriteriaQuery<T> query, CriteriaBuilder cb) {
        query.where((Predicate[]) expressions.toArray());
    }
}
