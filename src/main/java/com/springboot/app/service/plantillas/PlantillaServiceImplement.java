package com.springboot.app.service.plantillas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.springboot.app.constantes.Constantes;

public class PlantillaServiceImplement {
	public static void generadorServiceImplement(String motor, String columna, String host, String user, String password,
			String eschema) throws SQLException, IOException {
		
		Connection con = Constantes.ObtenerConexion(motor, host, eschema, user, password);
		
		// OBTENER LA METADATA
		DatabaseMetaData met = con.getMetaData();
		
		// CAPITALIZAR EL NOMBRE DE LA TABLA
		String tablaVar = Constantes.CapitalizarPrimeraMayus(columna);
		
		// VARIABLES
		String primaryType = Constantes.TipoDatoPrim(motor, columna, host, eschema, user, password);
		String llave = Constantes.LlavePrim(motor, columna, host, eschema, user, password);
		
		BufferedWriter implement = new BufferedWriter(
				new FileWriter( Constantes.Ruta() + "\\" +motor+ "\\"+ eschema + "\\" + tablaVar + "\\" + tablaVar
						+ "ServiceImplement.java"));
		implement.write("import java.util.List;\r\n" 
				+ "import java.util.Optional;\r\n" + "\r\n"
				+ "import org.springframework.beans.factory.annotation.Autowired;\r\n"
				+ "import org.springframework.stereotype.Service;\r\n"
				+ "import org.springframework.transaction.annotation.Transactional;\r\n" + "\r\n" 
				+ "@Service\r\n"
				+ "public class " + tablaVar + "ServiceImplement implements I" + tablaVar + "Service {\r\n" + "\r\n"
				+ "	@Autowired\r\n" 
				+ "	private I" + tablaVar + "Dao " + columna.toLowerCase() + "Dao;\r\n" + "\r\n"
				+ "	@Override\r\n" 
				+ "	@Transactional(readOnly = true)\r\n" 
				+ "	public List<" + tablaVar + "> findAll() {\r\n" 
				+ "		return (List<" + tablaVar + ">) " + columna.toLowerCase() + "Dao.findAll();\r\n" 
				+ "	}\r\n" + "\r\n" 
				+ "	@Override\r\n" 
				+ "	@Transactional\r\n" 
				+ "	public " + tablaVar + " save(" + tablaVar + " " + columna.toLowerCase() + ") {\r\n" 
				+ "		return " + columna.toLowerCase() + "Dao.save(" + columna.toLowerCase() + ");\r\n" 
				+ "	}\r\n" + "\r\n"
				+ "	@Override\r\n" 
				+ "	@Transactional(readOnly = true)\r\n" 
				+ "	public " + tablaVar + " findOne("+primaryType+" id) {\r\n" 
				+ "		return " + columna.toLowerCase() + "Dao.findById(id).orElse(null);\r\n" + "	}\r\n" + "\r\n" 
				+ "	@Override\r\n"
				+ "	@Transactional\r\n" 
				+ "	public boolean delete("+primaryType+" id) {\r\n" 
				+ "		Optional<" + tablaVar + "> conex = " + columna.toLowerCase() + "Dao.findById(id);\r\n"
				+ "		if (!conex.isEmpty()) {\r\n" 
				+ "			" + columna.toLowerCase() + "Dao.deleteById(id);\r\n" 
				+ "			return true;\r\n" 
				+ "		}\r\n"
				+ "		return false;\r\n" 
				+ "	}\r\n" + "\r\n" 
				+ "	@Override\r\n" 
				+ "	@Transactional\r\n"
				+ "	public " + tablaVar + " update("+primaryType+" id, " + tablaVar + " detalles" + columna.toLowerCase()+ ") {\r\n		" 
				+ tablaVar + " " + columna.toLowerCase() + " = " + columna.toLowerCase() + ".findById(id).get();\r\n");
		
		//OBTENGO LAS COLUMNAS NUEVAMENTE PARA ESCRIBIR DINAMICAMENTE LOS .SET DEL METODO UPDATE
		try (ResultSet columns2 = met.getColumns(eschema, null, columna, null)) {
			while (columns2.next()) {
				String columnName2 = columns2.getString("COLUMN_NAME");
				String columna2Var = Constantes.CapitalizarMinus(columnName2);
				implement.write("		" + columna.toLowerCase() + ".set" + columna2Var + "(detalles" + columna.toLowerCase() + ".get" + columna2Var + "());\r\n");
			}
		implement.write("		"+ columna.toLowerCase() + "Dao.save("+columna.toLowerCase()+");\r\n"
				+ "		return detalles" + columna.toLowerCase() + ";\n" 
				+ "	}\n"+ "\r\n"
				+ " 	@Override\r\n" 
				+ "	@Transactional(readOnly = true)\r\n"
				+ "	public List<" + tablaVar + "> find" + llave + "Men("+primaryType+" " + llave.toLowerCase() + ") {\r\n"
				+ "		Specification<" + tablaVar + "> specifications = Specification\r\n" 
				+ "				.where("+ tablaVar + "Specification.has" + llave + "(" + llave.toLowerCase() + "));\r\n" 
				+ "		return " + columna.toLowerCase() + "Dao.findAll(specifications);\r\n" 
				+ "	}\r\n"
				+ "}");
		
		implement.close();
		
		}
	}
}
