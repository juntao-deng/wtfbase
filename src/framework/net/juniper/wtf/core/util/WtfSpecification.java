package net.juniper.wtf.core.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.sf.json.JSONObject;

import org.springframework.data.jpa.domain.Specification;

public class WtfSpecification<T> implements Specification<T> {
	private JSONObject filter;
	public WtfSpecification(JSONObject filter){
		this.filter = filter;
	}
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		return null;
	}

}