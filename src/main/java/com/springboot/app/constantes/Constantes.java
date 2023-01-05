package com.springboot.app.constantes;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;


public class Constantes {

	// CONVERTIR TIPO DE DATO COMPATIBLE A JAVA
	public static String ConstantesTipo(String tipoDatMod) {
		if (tipoDatMod.equals("VARCHAR") || tipoDatMod.equals("VARCHAR2") || tipoDatMod.equals("varchar")) {
			return "String";
		} else if (tipoDatMod.equals("NUMBER") || tipoDatMod.equals("serial") || tipoDatMod.equals("INT")) {
			return "int";
		} else {
			return CapitalizarPrimeraMayus(tipoDatMod);
		}
	}

	// CONVERTIR TIPO DE DATO POSTGRESS
	public static String TipoDao(String tipo) {
		try {
			if (tipo.equals("int")) {
				return "Integer";
			} else {
				return null;
			}
		} catch (Exception e) {
		}
		return "";
	}

	// CAPITALIZAR PRIMERA LETRA MAYUS
	public static String CapitalizarPrimeraMayus(String cap) {
		return cap.toUpperCase().substring(0, 1) + cap.toLowerCase().substring(1);
	}

	// CAPITALIZAR TODO A MINUS
	public static String CapitalizarMinus(String cap) {
		return cap.toLowerCase();
	}

	// OBTENGO LA CONEXION
	public static Connection ObtenerConexion(String motor, String host, String eschema, String user, String password)
			throws SQLException {
		if (motor.equals("oracle")) {
			return DriverManager.getConnection("jdbc:oracle:thin:@//" + host + "/" + eschema, user, password);
		} else if (motor.equals("postgres")) {
			return DriverManager.getConnection("jdbc:postgresql://" + host + "/" + eschema, user, password);
		} else if (motor.equals("mysql")) {
			return DriverManager.getConnection("jdbc:mysql://" + host + "/" + eschema, user, password);
		} else {
			return null;
		}
	}

	// OBTENGO LA LLAVE PRIMARIA
	public static String LlavePrim(String motor, String columna, String host, String eschema, String user,
			String password) throws SQLException {

		String llave = "";

		Connection con = ObtenerConexion(motor, host, eschema, user, password);
		DatabaseMetaData met = con.getMetaData();

		try (ResultSet columns = met.getPrimaryKeys(eschema, null, columna)) {
			while (columns.next()) {
				String columnName = columns.getString("COLUMN_NAME");
				llave = CapitalizarPrimeraMayus(columnName);
				break;
			}
		}
		return llave;
	}
	
	// OBTENGO EL TIPO DE DATO DE LA LLAVE PRIMARIA
	public static String TipoDatoPrim(String motor, String columna, String host, String eschema, String user,
			String password) throws SQLException {

		String dato = "";

		Connection con = ObtenerConexion(motor, host, eschema, user, password);
		DatabaseMetaData met = con.getMetaData();

		try (ResultSet columns = met.getColumns(eschema, null, columna, null)) {
			while (columns.next()) {
				String datatype = columns.getString("TYPE_NAME");
				dato = Constantes.ConstantesTipo(datatype);
			}
		}
		return dato;
	}
	
	// ESCRIBIR
	public static String EscribirSerial(Boolean serial) {
		if (serial == true) {
			return "	@GeneratedValue(strategy = GenerationType.IDENTITY)\n";
		} else {
			return "";
		}
	}
	
	// ENTITY
		// OBTENGO MIS COLUMNAS FORANEAS
		public static List<String> ForaneasColumn(String motor, String columna, String host, String eschema, String user,
				String password) throws SQLException {

			List<String> listaforaneas = new ArrayList<String>();

			Connection con = ObtenerConexion(motor, host, eschema, user, password);
			DatabaseMetaData met = con.getMetaData();

			try (ResultSet foreign = met.getImportedKeys(eschema, null, columna)) {
				while (foreign.next()) {
					String fkColumnName = foreign.getString("FKCOLUMN_NAME");
					listaforaneas.add(fkColumnName);
				}
			}
			return listaforaneas;
		}

		// OBTENGO MIS TABLAS FORANEAS
		public static List<String> ForaneasTable(String motor, String columna, String host, String eschema, String user,
				String password) throws SQLException {

			List<String> foranea = new ArrayList<String>();

			Connection con = ObtenerConexion(motor, host, eschema, user, password);
			DatabaseMetaData met = con.getMetaData();

			try (ResultSet foreign = met.getImportedKeys(eschema, null, columna)) {
				while (foreign.next()) {
					String fkTableName = foreign.getString("PKTABLE_NAME");
					foranea.add(fkTableName);
				}
			}
			return foranea;
		}

		// OBTENGO MIS TABLAS FORANEAS CAPITALIZADAS PARA VALIDAR RELACION
		public static List<String> ExistenciaFora(String motor, String columna, String host, String eschema, String user,
				String password) throws SQLException {

			List<String> existenciaf = new ArrayList<String>();

			Connection con = ObtenerConexion(motor, host, eschema, user, password);
			DatabaseMetaData met = con.getMetaData();

			try (ResultSet foreign = met.getImportedKeys(eschema, null, columna)) {
				while (foreign.next()) {
					String fkTableName = foreign.getString("PKTABLE_NAME");
					existenciaf.add(Constantes.CapitalizarPrimeraMayus(fkTableName));
				}
			}
			return existenciaf;
		}

		// VALIDO PARA POSTGRESQL SI UNA LLAVE ES SERIAL PARA PONER LA ANOTACION CORRESPONDIENTE
		public static Boolean Serial(String motor, String columna, String host, String eschema, String user,
				String password) throws SQLException {

			Boolean serial = false;

			Connection con = ObtenerConexion(motor, host, eschema, user, password);
			DatabaseMetaData met = con.getMetaData();

			try (ResultSet columns = met.getColumns(null, null, columna, null)) {
				while (columns.next()) {
					String datatype = columns.getString("TYPE_NAME");
					if (datatype.equals("serial")) {
						serial = true;
					}
				}
			}
			return serial;
		}
		
	// CREO EL ARCHIVO Y LA RUTA PRINCIPAL EN DOCUMENTOS
	public static File Ruta() {
		String nuevac = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
		File carpeta = new File(nuevac+"\\Generador");
		return carpeta;
	}
		
}
