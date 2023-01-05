package com.springboot.app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.springboot.app.entity.ConexionPostgres;

public interface IConexionPostgresService {
	
	public List<ConexionPostgres> findAll();

	public ConexionPostgres save(ConexionPostgres conexionPostgres);

	public ConexionPostgres findOne(int id);

	public boolean delete(int id);

	public ConexionPostgres update(int id, ConexionPostgres conexionPostgres);
	
	public List<String> getMetadata(String host, String schema, String user, String password)throws SQLException;
	
	public List<String> generador(String motor, String columna, String host, String user, String password, String eschema)throws SQLException,IOException;
	
	public List<ConexionPostgres> findCodigoMen(int codigo, String conexion);
	
	public Boolean relaciones(String host, String columna, String schema, String user, String password, String motor) throws SQLException;
}
