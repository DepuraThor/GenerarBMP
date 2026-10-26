package BMP;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BMP {

	public void generarBmpPorcentajes(File carpetaPadre, long tamañoTotal) {
		if (carpetaPadre == null || !carpetaPadre.exists() || tamañoTotal == 0) {
			return;
		}

		// Filtramos
		File[] subCarpetasArray = carpetaPadre.listFiles(pathname -> pathname.isDirectory());
		if (subCarpetasArray == null || subCarpetasArray.length == 0) {
			return;
		}
		List<File> subCarpetas = new ArrayList<>();
		for (File f : subCarpetasArray) {
			subCarpetas.add(f);
		}
		Collections.sort(subCarpetas);

		long[] tamañosSub = new long[subCarpetas.size()];
		for (int i = 0; i < subCarpetas.size(); i++) {
			tamañosSub[i] = obtenerTamañoCarpeta(subCarpetas.get(i));
		}
		// Tamaño de la imagem
		int ancho = 600;
		int alto = 400;
		// Tiene que ser multiplo de 4 o is no se raya
		int tamañoFila = ((ancho * 3 + 3) / 4) * 4;
		int tamañoPixelArray = tamañoFila * alto;
		int tamañoFichero = 54 + tamañoPixelArray;
		// Para que aparezca en el proyecto
		File archivoSalida = new File(System.getProperty("user.dir"), "porcentajes_carpetas.bmp");

		try (FileOutputStream fos = new FileOutputStream(archivoSalida);
				BufferedOutputStream bos = new BufferedOutputStream(fos)) {

			bos.write('B');
			bos.write('M');
			escribirEntero32bytes(bos, tamañoFichero);
			escribirEntero16bytes(bos, 0);
			escribirEntero16bytes(bos, 0);
			escribirEntero32bytes(bos, 54);

			escribirEntero32bytes(bos, 40);
			escribirEntero32bytes(bos, ancho);
			escribirEntero32bytes(bos, alto);
			escribirEntero16bytes(bos, 1);
			escribirEntero16bytes(bos, 24);
			escribirEntero32bytes(bos, 0);
			escribirEntero32bytes(bos, tamañoPixelArray);
			escribirEntero32bytes(bos, 2835);
			escribirEntero32bytes(bos, 2835);
			escribirEntero32bytes(bos, 0);
			escribirEntero32bytes(bos, 0);

			// Dibujar barras horizontales y fondo
			for (int y = 0; y < alto; y++) {
				int indiceBarra = -1;
				if (tamañosSub.length > 0) {
					int alturaBarra = alto / Math.max(1, tamañosSub.length);
					indiceBarra = (alto - 1 - y) / alturaBarra;
				}

				for (int x = 0; x < ancho; x++) {
					int azul = 255, verde = 255, rojo = 255;

					if (x % 60 == 0) {
						azul = 255;
						verde = 0;
						rojo = 0;
					}

					if (indiceBarra >= 0 && indiceBarra < tamañosSub.length) {
						long tamMax = tamañoTotal > 0 ? tamañoTotal : 1;
						int longitudBarra = (int) ((tamañosSub[indiceBarra] * (ancho - 50)) / tamMax);

						if (x < longitudBarra) {
							if (indiceBarra % 3 == 0) {
								azul = 0;
								verde = 0;
								rojo = 255;
							} else if (indiceBarra % 3 == 1) {
								azul = 128;
								verde = 0;
								rojo = 128;
							} else {
								azul = 255;
								verde = 128;
								rojo = 0;
							}
						}
					}

					bos.write(azul);
					bos.write(verde);
					bos.write(rojo);
				}

				int padding = tamañoFila - (ancho * 3);
				for (int p = 0; p < padding; p++) {
					bos.write(0);
				}
			}

			System.out.println("BMP generado con exito en: " + archivoSalida.getAbsolutePath());

		} catch (IOException e) {
			System.out.println("Error al generar el BMP: " + e.getMessage());
		}
	}

// Tomamos el tamaño de la carpeta
	private long obtenerTamañoCarpeta(File carpeta) {
		if (carpeta == null || !carpeta.exists())
			return 0;
		if (carpeta.isFile())
			return carpeta.length();

		long suma = 0;
		File[] ficheros = carpeta.listFiles();
		if (ficheros != null) {
			for (File f : ficheros) {
				suma += obtenerTamañoCarpeta(f);
			}
		}
		return suma;
	}

	private void escribirEntero16bytes(BufferedOutputStream bos, int valor) throws IOException {
		bos.write(valor & 0xFF);
		bos.write((valor >> 8) & 0xFF);
	}

	private void escribirEntero32bytes(BufferedOutputStream bos, int valor) throws IOException {
		bos.write(valor & 0xFF);
		bos.write((valor >> 8) & 0xFF);
		bos.write((valor >> 16) & 0xFF);
		bos.write((valor >> 24) & 0xFF);
	}
}