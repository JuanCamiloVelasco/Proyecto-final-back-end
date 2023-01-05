package com.springboot.app.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.springboot.app.entity.ConexionPostgres;

public interface IConexionPostgresDao extends CrudRepository<ConexionPostgres, Integer>, JpaSpecificationExecutor<ConexionPostgres> {

}
