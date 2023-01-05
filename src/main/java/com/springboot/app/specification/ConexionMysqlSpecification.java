package com.springboot.app.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.springboot.app.entity.ConexionMysql;


@Component
public class ConexionMysqlSpecification {
	public static Specification<ConexionMysql> hasCodigo(int codigo){
		return ((root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.le(root.get("id"), codigo);	
		});
	}

	public static Specification<ConexionMysql> hasUrl(String url){
		return ((root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("url"), url);	
		});
	}
}
