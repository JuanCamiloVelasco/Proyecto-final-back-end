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

import com.springboot.app.entity.ConexionMysql;
import com.springboot.app.service.IConexionMysqlService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ConexionMysqlController {

	@Autowired
	private IConexionMysqlService conexionMysqlService;

	@GetMapping("/conexionesmysql")
	public List<ConexionMysql> getAllConexiones() {
		return conexionMysqlService.findAll();
	}

	@GetMapping("/conexionesmysql/{id}")
	public ResponseEntity<ConexionMysql> getConexionById(@PathVariable int id) {
		ConexionMysql conexion = conexionMysqlService.findOne(id);
		return ResponseEntity.ok(conexion);
	}

	@PostMapping("/conexionesmysql")
	public ConexionMysql crearConexion(@RequestBody ConexionMysql conexion) {
		return conexionMysqlService.save(conexion);
	}

	@PutMapping("/conexionesmysql/{id}")
	public ResponseEntity<ConexionMysql> updateConexion(@PathVariable int id,
			@RequestBody ConexionMysql detallesconexion) {
		detallesconexion = conexionMysqlService.update(id, detallesconexion);
		return ResponseEntity.ok(detallesconexion);
	}

	@DeleteMapping("/conexionesmysql/{id}")
	public boolean deleteConexion(@PathVariable int id) {
		return conexionMysqlService.delete(id);
	}

	// METADATA

	// OBTENER TABLAS
	@GetMapping("/metadatamysql/{host}/{eschema}/{user}/{password}")
	public List<String> getTablasMeta(@PathVariable String host, @PathVariable String user,
			@PathVariable String password, @PathVariable String eschema) throws SQLException {
		return conexionMysqlService.getMetadata(host, eschema, user, password);
	}

	// GENERADOR DE CODIGO
	@GetMapping("/metadataGenmysql/{motor}/{host}/{eschema}/{user}/{password}/{columna}")
	public List<String> genColumn(@PathVariable String motor, @PathVariable String columna, @PathVariable String host,
			@PathVariable String user, @PathVariable String password, @PathVariable String eschema)
			throws SQLException, IOException {
		return conexionMysqlService.generador(motor, columna, host, user, password, eschema);
	}

	// CRITERIA
	@GetMapping("/criteriamysql/{codigo}/{conexion}")
	public List<ConexionMysql> getCodigoMen(@PathVariable int codigo, @PathVariable String conexion) {
		return conexionMysqlService.findCodigoMen(codigo, conexion);
	}

	// VALIDAR RELACIONES
	@GetMapping("/relacionesmysql/{host}/{eschema}/{user}/{password}/{columna}/{motor}")
	public Boolean validarRelaciones(@PathVariable String host, @PathVariable String eschema, @PathVariable String user,
			@PathVariable String password, @PathVariable String columna, @PathVariable String motor) throws SQLException {
		return conexionMysqlService.relaciones(host, columna, eschema, user, password, motor);
	}
}
