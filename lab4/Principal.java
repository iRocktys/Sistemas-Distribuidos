/**
  * Laboratorio 4  
  * Autor: Leandro Martins Tosta e Eiti Parruca Adama
  * Ultima atualizacao: 29/04/2024
  */
package lab4;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Principal {

	public final static Path path = Paths
			.get("src\\fortunes_openbsd.txt");
	private int NUM_FORTUNES = 0;

	private FileReader fr;

	public class FileReader {

		public int countFortunes() throws FileNotFoundException {

			int lineCount = 0;

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();

				} // fim while

				System.out.println(lineCount);
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return lineCount;
		}

		public void parser(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				int lineCount = 0;

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();
					StringBuffer fortune = new StringBuffer();
					while (!(line == null) && !line.equals("%")) {
						fortune.append(line + "\n");
						line = br.readLine();
						// System.out.print(lineCount + ".");
					}

					hm.put(lineCount, fortune.toString());
					System.out.println(fortune.toString());

					System.out.println(lineCount);
				} // fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}

		public String read(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			String result = "-2";

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				SecureRandom sr = new SecureRandom();
				int lineSelected = sr.nextInt(NUM_FORTUNES);

				System.out.println(lineSelected);

				System.out.println(hm.get(lineSelected));

				result = hm.get(lineSelected);

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return result;
		}

		public void write(HashMap<Integer, String> hm, String fortune)
				throws FileNotFoundException {

			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					path.toString(), true)); // true=append
			try (BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(os))) {

				Scanner input = new Scanner(System.in);
				// System.out.print("Add fortune: ");
				// String fortune = input.next();

				NUM_FORTUNES++;

				hm.put(NUM_FORTUNES, fortune);

				System.out.println(hm.get(NUM_FORTUNES));

				// Append file
				bw.append("\n%\n" + fortune);

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}
	}
	
	public void write(String fortune) {
		fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap hm = new HashMap<Integer, String>();
			fr.parser(hm);
			fr.read(hm);
			fr.write(hm, fortune);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String read() {
		String result = "-1";

		fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap hm = new HashMap<Integer, String>();
			fr.parser(hm);
			result = fr.read(hm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

}
