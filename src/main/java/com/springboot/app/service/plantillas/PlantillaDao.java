package com.springboot.app.service.plantillas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.springboot.app.constantes.Constantes;

public class PlantillaDao {

	public static void generadorDao(String motor, String columna, String host, String user, String password, String eschema) 
			throws SQLException, IOException {
		
		//CREO LA CONEXION
		Connection con = Constantes.ObtenerConexion(motor, host, eschema, user, password);
		
		// OBTENER LA METADATA
		DatabaseMetaData met = con.getMetaData();
		
		// CAPITALIZAR EL NOMBRE DE LA TABLA
		String tablaVar = Constantes.CapitalizarPrimeraMayus(columna);
		
		//VARIABLES
		String primaryType = "";
		
		//OBTENGO LOS TIPOS DE DATOS INCOMPATIBLES CON JAVA Y LOS CONVIERTO A INT O STRING
		try (ResultSet columns = met.getColumns(eschema, null, columna, null)) {
			while (columns.next()) {
				String datatype = columns.getString("TYPE_NAME");
				primaryType = Constantes.TipoDao(Constantes.ConstantesTipo(datatype));
				break;
			}
		}
		
		BufferedWriter dao = new BufferedWriter(new FileWriter(
				Constantes.Ruta() + "\\" +motor+ "\\"+ eschema + "\\" + tablaVar + "\\I" + tablaVar + "Dao.java"));

		//ESCRIBO EL GENERADOR DE CODIGO
		dao.write("import org.springframework.data.repository.CrudRepository;\n"
				+ "import org.springframework.data.jpa.repository.JpaSpecificationExecutor;\r\n"
				+ "import org.springframework.data.repository.CrudRepository;\r\n" + "\r\n" 
				+ "public interface I" + tablaVar + "Dao CrudRepository<" + tablaVar + ", "+primaryType+">, JpaSpecificationExecutor<" + tablaVar + "> {\n"
				+ "\n }");
		dao.close();
	}
}