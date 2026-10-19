package BMP;

import java.io.File;

import javax.swing.JFileChooser;

public class dirSize {
	public void MostrarTamañoCarpeta() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int res = fc.showDialog(null, "Abrir");

		if (res != JFileChooser.APPROVE_OPTION) {
			System.out.println("Error en la eleccion. ");
		} else {
			String dir = fc.getSelectedFile().getAbsolutePath();
			File carpeta = new File(dir);
			System.out.println("Tamaño de la Carpeta:" + TamañoCarpeta(carpeta));
		}
	}

	private long TamañoCarpeta(File carpeta) {
		long size = 0;
		File[] archivos = carpeta.listFiles();
		for (File file : archivos) {
			size += file.length();
		}
		return size;
	}
}