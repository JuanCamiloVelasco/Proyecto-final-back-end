package com.springboot.app.service.plantillas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import com.springboot.app.constantes.Constantes;

public class PlantillaCriteria {
	public static void generadorCriteria(String motor, String columna, String host, String user, String password,
			String eschema) throws SQLException, IOException {

		// CAPITALIZAR EL NOMBRE DE LA TABLA
		String tablaVar = Constantes.CapitalizarPrimeraMayus(columna);

		// VARIABLES
		String llave = Constantes.LlavePrim(motor, columna, host, eschema, user, password);

		BufferedWriter criteria = new BufferedWriter(
				new FileWriter( Constantes.Ruta() + "\\" +motor+ "\\" + eschema + "\\" + tablaVar + "\\" + tablaVar
						+ "Specification.java"));
		criteria.write("import org.springframework.data.jpa.domain.Specification;\r\n"
				+ "import org.springframework.stereotype.Component;\r\n" + "\r\n"
				+ " @Component\r\n"
				+ " public class " + tablaVar + "Specification {\r\n" + "\r\n"
				+ "	public static Specification<" + tablaVar + "> has" + llave + "(" + "codigo){\r\n"
				+ "		return ((root, criteriaQuery, criteriaBuilder) -> {\r\n"
				+ "			return criteriaBuilder.equals(root.get("+"\""+llave.toLowerCase()+"\""+"), codigo);	\r\n" 
				+ "		});\r\n"
				+ "	}\r\n"
				+ "}");
		criteria.close();
	}
}
