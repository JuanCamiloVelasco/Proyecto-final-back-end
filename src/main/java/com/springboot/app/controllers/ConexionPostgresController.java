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

import com.springboot.app.entity.ConexionPostgres;
import com.springboot.app.service.IConexionPostgresService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ConexionPostgresController {
	
	@Autowired
	private IConexionPostgresService conexionPostgresService;

	@GetMapping("/conexionespost")
	public List<ConexionPostgres> getAllConexiones() {
		return conexionPostgresService.findAll();
	}

	@GetMapping("/conexionespost/{id}")
	public ResponseEntity<ConexionPostgres> getConexionById(@PathVariable int id) {
		ConexionPostgres conexion = conexionPostgresService.findOne(id);
		return ResponseEntity.ok(conexion);
	}

	@PostMapping("/conexionespost")
	public ConexionPostgres crearConexion(@RequestBody ConexionPostgres conexion) {
		return conexionPostgresService.save(conexion);
	}

	@PutMapping("/conexionespost/{id}")
	public ResponseEntity<ConexionPostgres> updateConexion(@PathVariable int id, @RequestBody ConexionPostgres detallesconexion) {
		detallesconexion = conexionPostgresService.update(id, detallesconexion);
		return ResponseEntity.ok(detallesconexion);
	}

	@DeleteMapping("/conexionespost/{id}")
	public boolean deleteConexion(@PathVariable int id) {
		return conexionPostgresService.delete(id);
	}

	// METADATA

	// OBTENER TABLAS
	@GetMapping("/metadatapost/{host}/{eschema}/{user}/{password}")
	public List<String> getTablasMeta(@PathVariable String host, @PathVariable String user,
			@PathVariable String password, @PathVariable String eschema) throws SQLException {
		return conexionPostgresService.getMetadata(host, eschema, user, password);
	}

	// GENERADOR DE CODIGO
	@GetMapping("/metadataGenpost/{motor}/{host}/{eschema}/{user}/{password}/{columna}")
	public List<String> genColumn(@PathVariable String motor, @PathVariable String columna, @PathVariable String host, @PathVariable String user,
			@PathVariable String password, @PathVariable String eschema) throws SQLException, IOException {
		return conexionPostgresService.generador(motor, columna, host, user, password, eschema);
	}

	// CRITERIA
	@GetMapping("/criteriapost/{codigo}/{conexion}")
	public List<ConexionPostgres> getCodigoMen(@PathVariable int codigo, @PathVariable String conexion){
		return conexionPostgresService.findCodigoMen(codigo, conexion);
	}
	
	// VALIDAR RELACIONES
	@GetMapping("/relacionespost/{host}/{eschema}/{user}/{password}/{columna}/{motor}")
	public Boolean validarRelaciones(@PathVariable String host, @PathVariable String eschema, @PathVariable String user,
				@PathVariable String password, @PathVariable String columna, @PathVariable String motor) throws SQLException {
		return conexionPostgresService.relaciones(host, columna, eschema, user, password, motor);
	}
}
