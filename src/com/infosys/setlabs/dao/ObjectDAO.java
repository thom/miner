package com.infosys.setlabs.dao;

/**
 * Represents the base interface for all data access interfaces. Data access
 * interfaces encapsulate access to data sources.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 * @param <Type>
 *            Class of a domain object.
 */
public interface ObjectDAO<Type> extends ReadObjectDAO<Type>, WriteObjectDAO<Type> {
}