package BMP;

import java.io.File;

import javax.swing.JFileChooser;

public class dirSize {
	public void MostrarTamañoCarpeta() {
		// Usamos filechooser para hacer que el usuario escoja una carpeta.
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int res = fc.showDialog(null, "Abrir");

		// Vemos si escogio un archivo valido.
		if (res != JFileChooser.APPROVE_OPTION) {
			System.out.println("Error en la eleccion. ");
		} else {
			// Si si guardamos su path y lo usamos para inicializar un file.
			String dir = fc.getSelectedFile().getAbsolutePath();
			File carpeta = new File(dir);
			// optenemos el tamaño total de la carpeta mediante el metodo de TamañoCarpetas.
			Long tamañoCarpeta = TamañoCarpetas(carpeta);
			// Imprimimos el nombre de la carpeta con su tamaño.
			System.out.println("Tamaño de la Carpeta '" + carpeta.getName() + "': " + tamañoCarpeta + " bytes.");
			System.out.println(" ");

			// A continuacion vemos las carpetas internas y hacemos el mismo procedimiento
			// con cada una.
			System.out.println("Tamaño de las carpetas internas: ");
			// Hacemos un array de subcarpetas tipo file.
			File[] subCarpetas = carpeta.listFiles();
			// Si el array no esta vacio.
			if (subCarpetas != null) {
				// lo recorremos con un foreach.
				for (File file : subCarpetas) {
					// Si es una carpeta usamos el metodo TamañoCarpetas para sacar su tamaño.
					if (file.isDirectory()) {
						long tamañoSubCarpeta = TamañoCarpetas(file);
						// Imprimimos cada subcarpeta con su tamaño.
						System.out
								.println("- Subcarpeta " + file.getName() + " Tamaño: " + tamañoSubCarpeta + " bytes.");
					} else {
						System.out.println("No es una subcarpeta.");
					}
				}
			}
		}
	}

	// Metodo que debuelve el tamaño de las carpetas.
	private long TamañoCarpetas(File carpeta) {
		// si la carpeta esta vacia o no existe devuelve tamaño 0.
		if (carpeta == null || !carpeta.exists()) {
			return 0;
		}

		// Si es un fichero normal devolvemos su tamaño directamente.
		if (carpeta.isFile()) {
			return carpeta.length();
		}

		// Si es una carpeta, iteramos sobre todo su contenido y sumamos los tamaños.
		long sumTamañoInterna = 0;
		File[] ficheros = carpeta.listFiles(); //

		if (ficheros != null) {
			for (File f : ficheros) {
				sumTamañoInterna += TamañoCarpetas(f);
			}
		}
		return sumTamañoInterna;
	}
}