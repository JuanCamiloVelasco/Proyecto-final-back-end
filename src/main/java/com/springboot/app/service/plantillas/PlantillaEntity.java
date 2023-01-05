package com.springboot.app.service.plantillas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.springboot.app.constantes.Constantes;

public class PlantillaEntity {
	
	public static void generadorEntity(String motor, String columna, String host, String user, String password,
			String eschema) throws SQLException, IOException {
		
		// CREAR LA CONEXION
		Connection con = Constantes.ObtenerConexion(motor, host, eschema, user, password);
		
		// OBTENER LA METADATA
		DatabaseMetaData met = con.getMetaData();
		
		// CAPITALIZAR EL NOMBRE DE LA TABLA
		String tablaVar = Constantes.CapitalizarPrimeraMayus(columna);
		
		// VARIABLES
		String listaCap = "";
		Boolean serial = Constantes.Serial(motor, columna, host, eschema, user, password);
		
		// LISTAS
		List<String> foraneasColumn = Constantes.ForaneasColumn(motor, columna, host, eschema, user, password);
		List<String> foraneaTable = Constantes.ForaneasTable(motor, columna, host, eschema, user, password);
		List<String> existenciaf =  Constantes.ExistenciaFora(motor, columna, host, eschema, user, password);
		
		List<String> tipof = new ArrayList<String>();
		List<Boolean> existe = new ArrayList<>();
		
		// COMPROBAR EXISTECIA DE ARCHIVOS PARA RELACIONES
		for(int i=0; i<existenciaf.size();i++) {
			String filePath = Constantes.Ruta() + "\\" + motor + "\\" + eschema + "\\" + existenciaf.get(i) + "\\" + existenciaf.get(i) + "Entity.java";	 
	        Path path = Paths.get(filePath);
	        boolean exists = Files.exists(path);
	        existe.add(exists);
		}
		
		// GENERACION CODIGO ENTIDAD
		// OBTENGO TODAS LAS TABLAS DE LA BASE DE DATOS CONECTADA
		try (ResultSet columns = met.getColumns(eschema, null, columna, null)) {
			// CREO UN ARCHIVO .JAVA CON NOMBRE DINAMICO
			BufferedWriter entity = new BufferedWriter(new FileWriter(Constantes.Ruta()+"\\"
					+ motor + "\\"+ eschema +"\\" + tablaVar + "\\" + tablaVar + "Entity.java"));
			// ESCRIBO EL CODIGO DE GENERACION DE ENTIDADES
			entity.write("import javax.persistence.Column;\r\n" 
					+ "import javax.persistence.Entity;\r\n"
					+ "import javax.persistence.GeneratedValue;\r\n" 
					+ "import javax.persistence.GenerationType;\r\n"
					+ "import javax.persistence.Id;\r\n" 
					+ "import javax.persistence.Table;\r\n"
					+ "import javax.persistence.Table;\r\n"
					+ "\r\n" 
					+ "@Entity\n" 
					+ "@Table(name = " + columna + ")\n" 
					+ "public class " + tablaVar + " {\n"
					+ "\n" 
					+ "	@Id\n"
					+ Constantes.EscribirSerial(serial));
					
			
			// OBTENGO EL NOMBRE DE LAS COLUMNAS Y TIPO DE DATOS
			while (columns.next()) {
				String datatype = columns.getString("TYPE_NAME");
				String columnName = columns.getString("COLUMN_NAME");
				
				// CAPITALIZO LOS NOMBRES Y DATA TYPE
				String columnaVar = Constantes.CapitalizarMinus(columnName);
				String tipoDato = Constantes.ConstantesTipo(datatype);
	
				// VALIDO LA EXISTENCIA DE LA TABLA RELACIONADA
				if (!foraneasColumn.contains(columnName)) {
					entity.write("	@Column (name = " + columnName + ")\n" 
							+ "	private " + tipoDato + " " + columnaVar + ";\n" + "\n");
				}
				tipof.add(tipoDato);
			}
			for(int a = 0; a<existe.size();a++) {
				
				listaCap = Constantes.CapitalizarPrimeraMayus(foraneaTable.get(a));
				
				if(existe.get(a)==false) {
					entity.write("	@Column (name = " + foraneasColumn.get(a) + ")\n" 
							+ "	private " + tipof.get(a) + " " + foraneasColumn.get(a) + ";\n" + "\n");
				} else {
					entity.write("	@JoinColumn(name= " + foraneaTable.get(a) + ")\n"
							+ "	public List<" + listaCap + "> " + foraneaTable.get(a) + ";\n"+ "\n");
				}
			}
			try (ResultSet columns2 = met.getColumns(eschema, null, columna, null)) {
				while (columns2.next()) {
					String datatype = columns2.getString("TYPE_NAME");
					String columnName = columns2.getString("COLUMN_NAME");
					String tipoDato = Constantes.ConstantesTipo(datatype);
					
					// CAPITALIZO LOS NOMBRES Y DATA TYPE
					String columnaVar = Constantes.CapitalizarMinus(columnName);
					String tipoDatoCap = Constantes.CapitalizarPrimeraMayus(columnName);
					
					if (!foraneasColumn.contains(columnName)) {
						entity.write( " 	public "+ tipoDato + " get" + tipoDatoCap+"() {" + "\n"
								 	+ "		return " + columnaVar.toLowerCase() + "; \n"
								 	+ " 	} \n"
								 	+ "		\n"
								 	+ " 	public void set" + tipoDatoCap + "("+ tipoDato + " " + columnaVar + ") { \n"
								 	+ "		this."+columnaVar.toLowerCase()+" = " + columnaVar.toLowerCase()+"; \n"
								 	+ "	} \n"
								 	+ "		\n");
					}
				}
			}
			for(int a = 0; a<existe.size();a++) {
				String tipoColumnaCap = Constantes.CapitalizarPrimeraMayus(foraneasColumn.get(a));
				listaCap = Constantes.CapitalizarPrimeraMayus(foraneaTable.get(a));

				if(existe.get(a)==false) {
					entity.write( " 	public "+ tipof.get(a) + " get" + tipoColumnaCap +"() {" + "\n"
						 	+ "		return " + tipoColumnaCap.toLowerCase() + "; \n"
						 	+ " 	} \n"
						 	+ "		\n"
						 	+ " 	public void set" + tipoColumnaCap + "("+ tipof.get(a) + " " + tipoColumnaCap.toLowerCase() + ") { \n"
						 	+ "		this."+tipoColumnaCap.toLowerCase()+" = " + tipoColumnaCap.toLowerCase()+"; \n"
						 	+ "	} \n"
						 	+ "		\n");
				} else {
					entity.write( " 	public List<"+ listaCap + "> get" + listaCap +"() {" + "\n"
						 	+ "		return " + foraneaTable.get(a) + "; \n"
						 	+ " 	} \n"
						 	+ "		\n"
						 	+ " 	public void set" + listaCap + "(List<"+ listaCap + "> " + foraneaTable.get(a) + ") { \n"
						 	+ "		this."+foraneaTable.get(a)+" = " + foraneaTable.get(a) +"; \n"
						 	+ "	} \n"
						 	+ "		\n");
				}
			}
			entity.write("}");
			entity.close();
		}
	}
}
