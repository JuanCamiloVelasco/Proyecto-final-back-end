package com.springboot.app.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.constantes.Constantes;
import com.springboot.app.dao.IConexionDao;
import com.springboot.app.entity.Conexion;
import com.springboot.app.service.IConexionService;
import com.springboot.app.service.plantillas.PlantillaController;
import com.springboot.app.service.plantillas.PlantillaCriteria;
import com.springboot.app.service.plantillas.PlantillaDao;
import com.springboot.app.service.plantillas.PlantillaEntity;
import com.springboot.app.service.plantillas.PlantillaInterfaceService;
import com.springboot.app.service.plantillas.PlantillaServiceImplement;
import com.springboot.app.specification.ConexionSpecification;

@Service
public class ConexionServiceImplement implements IConexionService {

	@Autowired
	private IConexionDao conexionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Conexion> findAll() {
		return (List<Conexion>) conexionDao.findAll();
	}

	@Override
	@Transactional
	public Conexion save(Conexion conexion) {
		return conexionDao.save(conexion);
	}

	@Override
	@Transactional(readOnly = true)
	public Conexion findOne(int id) {
		return conexionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public boolean delete(int id) {
		Optional<Conexion> conex = conexionDao.findById(id);
		if (!conex.isEmpty()) {
			conexionDao.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public Conexion update(int id, Conexion detallesconexion) {
		Conexion conexion = conexionDao.findById(id).get();
		conexion.setUrl(detallesconexion.getUrl());
		conexion.setUsuario(detallesconexion.getUsuario());
		conexion.setContraseña(detallesconexion.getContraseña());
		conexion.setSchema(detallesconexion.getSchema());
		conexionDao.save(conexion);
		return detallesconexion;
	}

	Connection con = null;

	@Override
	@Transactional(readOnly = true)
	public List<String> getMetadata(String host, String schema, String user, String password) throws SQLException {
		con = DriverManager.getConnection("jdbc:oracle:thin:@//" + host + "/" + schema, user, password);
		DatabaseMetaData met = con.getMetaData();
		List<String> a = new ArrayList<>();
		try (ResultSet resultSet = met.getTables(schema, null, null, new String[] { "TABLE" })) {
			while (resultSet.next()) {
				String nombreTabla = resultSet.getString("TABLE_NAME");
				a.add(nombreTabla);
			}
		}
		return a;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> getForanea(String columna) throws SQLException {
		con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xe", "system", "1000854839");
		DatabaseMetaData met = con.getMetaData();
		List<String> a = new ArrayList<>();
		try (ResultSet foreign = met.getImportedKeys(null, null, columna)) {
			while (foreign.next()) {
				String columnName = foreign.getString("PKTABLE_NAME");
				String fkTableName = foreign.getString("FKTABLE_NAME");
				String pkColumnName = foreign.getString("PKCOLUMN_NAME");
				String fkColumnName = foreign.getString("FKCOLUMN_NAME");
				a.add(" PK TABLE NAME = " + columnName + " FT TABLE NAME = " + fkTableName + "PK COLUMNA NAME = "
						+ pkColumnName + " FK COLUMNA NAME = " + fkColumnName);
			}
			return a;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getMetadataColumna(String columna) throws SQLException {
		con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xe", "system", "1000854839");
		DatabaseMetaData met = con.getMetaData();
		List<String> a = new ArrayList<>();
		try (ResultSet columns = met.getColumns(null, null, columna, null)) {
			while (columns.next()) {
				String columnName = columns.getString("COLUMN_NAME");
				String columnSize = columns.getString("COLUMN_SIZE");
				String datatype = columns.getString("TYPE_NAME");
				String isNullable = columns.getString("IS_NULLABLE");
				String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");

				a.add(columnName + " " + columnSize + " " + datatype + " " + isNullable + " " + isAutoIncrement);
			}
		}
		return a;
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean relaciones(String host, String columna, String schema, String user, String password, String motor) throws SQLException {
		
		con = DriverManager.getConnection("jdbc:oracle:thin:@//" + host + "/" + schema, user, password);
		
		// OBTENER LA METADATA
		DatabaseMetaData met = con.getMetaData();
		
		String tablaF = "";
		Boolean response = false;
		List<String> foraneas = new ArrayList<>();
		List<Boolean> relaciones = new ArrayList<>();
		
		// OBTENGO LA METADATA DE LAS LLAVES FORANEAS (TABLAS Y COLUMNAS)
		try (ResultSet foreign = met.getImportedKeys(schema, null, columna)) {
			while (foreign.next()) {
				String fkTableName = foreign.getString("PKTABLE_NAME");
				tablaF = fkTableName.substring(0,1).toUpperCase() + fkTableName.toLowerCase().substring(1);
				foraneas.add(tablaF);
			}
		}
		
		// COMPROBAR EXISTECIA DE ARCHIVOS PARA RELACIONES	
		for(int i=0; i<foraneas.size();i++) {
			String filePath = Constantes.Ruta() + "\\"+ motor +"\\" + schema +"\\"+ foraneas.get(i) + "\\" + foraneas.get(i) + "Entity.java";	 
	        Path path = Paths.get(filePath);
	        boolean exists = Files.exists(path);
	        relaciones.add(exists);
		}
		
        for(int i=0; i<relaciones.size(); i++) {
			if (relaciones.get(i).equals(true)) {
				response = true;
			}
        }
        return response;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> generador(String motor,String columna, String host, String user, String password, String eschema)
			throws SQLException, IOException {

		// LISTA DE RETORNO
		List<String> a = new ArrayList<>();
		
		// CAPITALIZAR EL NOMBRE DE LA TABLA
		String tablaVar = Constantes.CapitalizarPrimeraMayus(columna);
		
		// CREACION DE LAS CARPETAS
		
		// UBICO LA RUTA DE DOCUMENTOS USANDO JFILECHOOSER Y CREO MI CARPETA PRINCIPAL
		File carpeta = Constantes.Ruta();
		carpeta.mkdir();
		
		// CREO MI CARPETA PARA EL MOTOR
		File directorio = new File(carpeta+"\\" +motor);
		directorio.mkdir();
		
		// CREO MI CARPETA PARA EL SCHEMA
		File directoriomotor = new File(directorio+"\\"+eschema);
		directoriomotor.mkdir();
		
		// CREO MI CARPETA PARA LAS TABLAS
		File directoriogenerador = new File(directoriomotor+"\\"+tablaVar);
		directoriogenerador.mkdir();

		// GENERACION CODIGO ENTIDAD
		PlantillaEntity.generadorEntity(motor, columna, host, user, password, eschema);

		// GENERADOR DE CODIGO DAO
		PlantillaDao.generadorDao(motor, columna, host, user, password, eschema);

		// GENERADOR DE CODIGO INTERFACE SERVICE
		PlantillaInterfaceService.generadorInterfaceService(motor, columna, host, user, password, eschema);

		// GENERADOR DE CODIGO SERVICE IMPLEMENT
		PlantillaServiceImplement.generadorServiceImplement(motor, columna, host, user, password, eschema);

		// GENERADOR DE CODIGO CONTROLLER
		PlantillaController.generadorController(motor, columna, host, user, password, eschema);

		// GENERADOR DE CODIGO CRITERIA
		PlantillaCriteria.generadorCriteria(motor, columna, host, user, password, eschema);

		// MENSAJE DE EXITO
		a.add("Codigo Generado con exito");

		// RETORNO
		return a;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Conexion> findCodigoMen(int codigo, String conexion) {
		Specification<Conexion> specifications = Specification
				.where(ConexionSpecification.hasCodigo(codigo).and(ConexionSpecification.hasUrl(conexion)));
		return conexionDao.findAll(specifications);
	}
}

/*
 * @Override
 * 
 * @Transactional public boolean delete(int id) { Optional<Conexion>
 * conex=conexionDao.findById(id); if(!conex.isEmpty()) {
 * conexionDao.deleteById(id); return true; } return false; }
 * 
 * 
 * 
 * 
 * 
 * @Override public Conexion update(int id, Conexion detallesconexion) {
 * Conexion conexion = conexionDao.findById(id).get();
 * conexion.setUrl(detallesconexion.getUrl());
 * conexion.setUsuario(detallesconexion.getUsuario());
 * conexion.setContraseña(detallesconexion.getContraseña());
 * conexionDao.save(conexion); return detallesconexion; }
 */
