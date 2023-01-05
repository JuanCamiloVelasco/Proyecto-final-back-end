package com.springboot.app.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.springboot.app.entity.Conexion;


@Component
public class ConexionSpecification {

	public static Specification<Conexion> hasCodigo(int codigo){
		return ((root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.le(root.get("id"), codigo);	
		});
	}

	public static Specification<Conexion> hasUrl(String url){
		return ((root, criteriaQuery, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("url"), url);	
		});
	}
	
}
