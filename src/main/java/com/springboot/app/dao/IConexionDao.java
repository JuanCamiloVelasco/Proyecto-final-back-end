package com.springboot.app.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.springboot.app.entity.Conexion;



public interface IConexionDao extends CrudRepository<Conexion, Integer>, JpaSpecificationExecutor<Conexion>{

}
