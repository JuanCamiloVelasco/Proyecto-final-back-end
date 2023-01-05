package com.springboot.app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.springboot.app.entity.ConexionMysql;


public interface IConexionMysqlService {
	
	public List<ConexionMysql> findAll();

	public ConexionMysql save(ConexionMysql conexionMysql);

	public ConexionMysql findOne(int id);

	public boolean delete(int id);

	public ConexionMysql update(int id, ConexionMysql conexionMysql);
	
	public List<String> getMetadata(String host, String schema, String user, String password)throws SQLException;
	
	public List<String> generador(String motor, String columna, String host, String user, String password, String eschema)throws SQLException,IOException;
	
	public List<ConexionMysql> findCodigoMen(int codigo, String conexion);
	
	public Boolean relaciones (String columna, String host, String schema, String user, String password, String motor)throws SQLException;
}
