package com.springboot.app.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.springboot.app.entity.ConexionMysql;


public interface IConexionMysqlDao extends CrudRepository<ConexionMysql, Integer>, JpaSpecificationExecutor<ConexionMysql> {

}
