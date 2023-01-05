package com.springboot.app.service.plantillas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import com.springboot.app.constantes.Constantes;

public class PlantillaInterfaceService {
	public static void generadorInterfaceService(String motor, String columna, String host, String user, String password,
			String eschema) throws SQLException, IOException {
		
		// CAPITALIZAR EL NOMBRE DE LA TABLA
		String tablaVar = Constantes.CapitalizarPrimeraMayus(columna);
		
		// VARIABLES
		String primaryType = Constantes.TipoDatoPrim(motor, columna, host, eschema, user, password);
		String llave = Constantes.LlavePrim(motor, columna, host, eschema, user, password);
		
		BufferedWriter iservice = new BufferedWriter(new FileWriter(
				Constantes.Ruta() + "\\" +motor+ "\\" + eschema + "\\" + tablaVar + "\\I" + tablaVar + "Service.java"));
		iservice.write("import java.util.List; \n"
				+ "\n"
				+ "public interface I" + tablaVar + "Service {\n"
				+ "\n"
				+ "	public List<" + tablaVar + "> findAll();\r\n" + "\r\n" 
				+ "	public " + tablaVar + " save("+ tablaVar + " " + columna.toLowerCase() + ");\r\n" + "\r\n" 
				+ "	public " + tablaVar + " findOne("+primaryType+" " + llave.toLowerCase() + ");\r\n" + "\r\n" 
				+ "	public boolean delete("+primaryType+" " + llave.toLowerCase() + ");\r\n" + "\r\n"
				+ " 	public List<"+tablaVar+"> find"+llave+"Men("+primaryType+" "+llave.toLowerCase()+");\r\n" 
				+ "\r\n }");
		iservice.close();
	}
}
