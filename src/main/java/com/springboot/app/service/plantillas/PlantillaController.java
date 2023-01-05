package com.springboot.app.service.plantillas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import com.springboot.app.constantes.Constantes;

public class PlantillaController {

	public static void generadorController(String motor, String columna, String host, String user, String password,
			String eschema) throws SQLException, IOException {

		// CAPITALIZAR EL NOMBRE DE LA TABLA
		String tablaVar = Constantes.CapitalizarPrimeraMayus(columna);

		// VARIABLES
		String primaryType = Constantes.TipoDatoPrim(motor, columna, host, eschema, user, password);
		String llave = Constantes.LlavePrim(motor, columna, host, eschema, user, password);

		BufferedWriter controller = new BufferedWriter(
				new FileWriter(Constantes.Ruta() + "\\" +motor+ "\\"+ eschema + "\\" + tablaVar + "\\" + tablaVar
						+ "Controller.java"));
		controller.write("import javax.validation.Valid;\r\n" + "\r\n"
				+ "import org.springframework.beans.factory.annotation.Autowired;\r\n"
				+ "import org.springframework.stereotype.Controller;\r\n"
				+ "import org.springframework.ui.Model;\r\n"
				+ "import org.springframework.validation.BindingResult;\r\n"
				+ "import org.springframework.web.bind.annotation.GetMapping;\r\n"
				+ "import org.springframework.web.bind.annotation.PathVariable;\r\n"
				+ "import org.springframework.web.bind.annotation.RequestMapping;\r\n"
				+ "import org.springframework.web.bind.annotation.RequestMethod;\r\n"
				+ "import org.springframework.web.bind.annotation.SessionAttributes;\r\n"
				+ "import org.springframework.web.bind.support.SessionStatus;\r\n" +"\r\n"
				+ "@CrossOrigin(origins = \"http://localhost:3000\")\r\n" 
				+ "@RestController\r\n"
				+ "public class " + tablaVar + "Controller {\r\n" + "\r\n" 
				+ "	@Autowired\r\n" 
				+ "	private I" + tablaVar + "Service " + columna.toLowerCase() + "Service;\r\n" + "\r\n" 
				+ "	@GetMapping("+"\""+"/" + columna.toLowerCase() +"\""+")\r\n" 
				+ "	public List<" + tablaVar + "> getAll" + tablaVar + "() {\r\n"
				+ "		return " + columna.toLowerCase() + "Service.findAll();\r\n" + "	}\r\n" + "\r\n"
				+ "	@PostMapping("+"\""+"/" + columna.toLowerCase() +"\""+", consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)\r\n" 
				+ "	public "+tablaVar+" crear" + tablaVar + "(@RequestBody " + tablaVar + " " + columna.toLowerCase() + ") {\r\n"
				+ "		return " + columna.toLowerCase() + "Service.save(" + columna.toLowerCase() + ");\r\n"
				+ "	}\r\n" + "\r\n"
				+ "	@GetMapping("+"\""+"/" + columna.toLowerCase() + "/{id}"+"\""+")\r\n" 
				+ "	public ResponseEntity<" + tablaVar + "> get" + tablaVar + "ById(@PathVariable "+primaryType+" id) {\r\n" 
				+ "		" + tablaVar + " " + columna.toLowerCase() + " = " + columna.toLowerCase() + "Service.findOne(id);\r\n"
				+ "		return ResponseEntity.ok(" + tablaVar + ");\r\n" 
				+ "	}\r\n"+ "\r\n" 
				+ "	@PutMapping("+"\""+"/" + columna.toLowerCase() + "/{id}"+"\""+", consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)\r\n" 
				+ "	public ResponseEntity<" + tablaVar + "> update" + tablaVar + "(@PathVariable "+primaryType+" id, @RequestBody " + tablaVar + " detalles" + columna.toLowerCase() + ") {\r\n" 
				+ "		" + columna.toLowerCase() + "conexion = " + columna.toLowerCase() + "Service.update(id , " + columna.toLowerCase() + "conexion);\r\n"
				+ "		return ResponseEntity.ok(" + columna.toLowerCase() + "conexion);\r\n" 
				+ "	}\r\n"
				+ "\r\n" 
				+ "	@DeleteMapping("+"\""+"/" + columna.toLowerCase() + "/{id}"+"\""+")\r\n" 
				+ "	public boolean delete" + tablaVar + "(@PathVariable "+primaryType+" id) {\r\n" 
				+ "		return " + columna.toLowerCase()+ "Service.delete(id);\r\n"
				+ " 	}\r\n" 
				+ "\r\n"
				+ "	@GetMapping("+"\"/criteria/{" + llave + "}"+"\""+")\r\n"
				+ "	public List<" + tablaVar + "> get" + llave + "Men(@PathVariable "+primaryType+" " + llave.toLowerCase() + "){\r\n"
				+ "		return " + columna.toLowerCase() + "Service.find" + llave + "Men(" + llave.toLowerCase() + ");\r\n"
				+ "	}\n" 
				+ "}");
		controller.close();
	}
}
