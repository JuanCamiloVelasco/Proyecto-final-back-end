package com.springboot.app.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.entity.Conexion;
import com.springboot.app.service.IConexionService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ConexionController {

	@Autowired
	private IConexionService conexionService;

	@GetMapping("/conexiones")
	public List<Conexion> getAllConexiones() {
		return conexionService.findAll();
	}

	@GetMapping("/conexiones/{id}")
	public ResponseEntity<Conexion> getConexionById(@PathVariable int id) {
		Conexion conexion = conexionService.findOne(id);
		return ResponseEntity.ok(conexion);
	}

	@PostMapping("/conexiones")
	public Conexion crearConexion(@RequestBody Conexion conexion) {
		return conexionService.save(conexion);
	}

	@PutMapping("/conexiones/{id}")
	public ResponseEntity<Conexion> updateConexion(@PathVariable int id, @RequestBody Conexion detallesconexion) {
		detallesconexion = conexionService.update(id, detallesconexion);
		return ResponseEntity.ok(detallesconexion);
	}

	@DeleteMapping("/conexiones/{id}")
	public boolean deleteConexion(@PathVariable int id) {
		return conexionService.delete(id);
	}

	// METADATA

	// OBTENER TABLAS
	@GetMapping("/metadata/{host}/{eschema}/{user}/{password}")
	public List<String> getTablasMeta(@PathVariable String host, @PathVariable String user,
			@PathVariable String password, @PathVariable String eschema) throws SQLException {
		return conexionService.getMetadata(host, eschema, user, password);
	}

	// LLAVE FORANEA
	@GetMapping("/metaforanea/{columna}")
	public List<String> getForanea(@PathVariable String columna) throws SQLException {
		return conexionService.getForanea(columna);
	}

	// COLUMNAS
	@GetMapping("/metadata/{columna}")
	public List<String> getColumMeta(@PathVariable String columna) throws SQLException {
		return conexionService.getMetadataColumna(columna);
	}

	// GENERADOR DE CODIGO
	@GetMapping("/metadataGen/{motor}/{host}/{eschema}/{user}/{password}/{columna}")
	public List<String> genColumn(@PathVariable String motor, @PathVariable String columna, @PathVariable String host, @PathVariable String user,
			@PathVariable String password, @PathVariable String eschema) throws SQLException, IOException {
		return conexionService.generador(motor, columna, host, user, password, eschema);
	}

	// CRITERIA
	@GetMapping("/criteria/{codigo}/{conexion}")
	public List<Conexion> getCodigoMen(@PathVariable int codigo, @PathVariable String conexion){
		return conexionService.findCodigoMen(codigo, conexion);
	}
	
	// VALIDAR RELACIONES
	@GetMapping("/relaciones/{host}/{eschema}/{user}/{password}/{columna}/{motor}")
	public Boolean validarRelaciones(@PathVariable String host, @PathVariable String eschema, @PathVariable String user,
			@PathVariable String password, @PathVariable String columna, @PathVariable String motor) throws SQLException {
		return conexionService.relaciones(host, columna, eschema, user, password, motor);
	}
	
}

//Connection con = null;
//@GetMapping("/metadata")
//public List<String> getTablasMeta() throws SQLException {
//	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/listaconexiones?useSSL=false&serverTimezone=UTC", "root" , "1000854839");
//	DatabaseMetaData met = con.getMetaData();
//	List<String> a = new ArrayList<>();
//	try(ResultSet resultSet = met.getTables(null, null, null, new String[]{"TABLE"})){
//		while(resultSet.next()) {
//			String nombreTabla = resultSet.getString("TABLE_NAME");
//			a.add(nombreTabla);
//		}
//	}
//	return a;
//}
//@GetMapping("/metadata/{columna}")
//public List<String> getColumMeta(@PathVariable String columna) throws SQLException {
//	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/listaconexiones?useSSL=false&serverTimezone=UTC", "root" , "1000854839");
//	DatabaseMetaData met = con.getMetaData();
//	List<String> a = new ArrayList<>();
//	try(ResultSet columns = met.getColumns(null, null, columna , null)){
//		while(columns.next()) {
//			String columnName = columns.getString("COLUMN_NAME");
//			String columnSize = columns.getString("COLUMN_SIZE");
//			String datatype = columns.getString("DATA_TYPE");
//			String isNullable = columns.getString("IS_NULLABLE");
//			String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
//			
//			a.add(columnName+" "+columnSize+" "+datatype+" "+isNullable+" "+isAutoIncrement);
//		}
//	}
//	return a;
//}


