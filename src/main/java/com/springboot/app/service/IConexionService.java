package com.springboot.app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.springboot.app.entity.Conexion;

public interface IConexionService {

	public List<Conexion> findAll();

	public Conexion save(Conexion conexion);

	public Conexion findOne(int id);

	public boolean delete(int id);

	public Conexion update(int id, Conexion conexion);
	
	public List<String> getMetadataColumna(String columna) throws SQLException;
	
	public List<String> getMetadata(String host, String schema, String user, String password)throws SQLException;
	
	public List<String> getForanea(String columna)throws SQLException;
	
	public List<String> generador(String motor, String columna, String host, String user, String password, String eschema)throws SQLException,IOException;
	
	public List<Conexion> findCodigoMen(int codigo, String conexion);
	
	public Boolean relaciones(String host, String columna, String schema, String user, String password, String motor) throws SQLException;
}
