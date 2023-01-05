package com.springboot.app.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.springboot.app.entity.ConexionPostgres;

@Component
public class ConexionPostgresSpecification {
	public static Specification<ConexionPostgres> hasCodigo(int codigo){
		return ((root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.le(root.get("id"), codigo);	
		});
	}

	public static Specification<ConexionPostgres> hasUrl(String url){
		return ((root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("url"), url);	
		});
	}
}
