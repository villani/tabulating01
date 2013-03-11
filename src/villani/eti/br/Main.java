package villani.eti.br;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

	public static LogBuilder log;
	public static TreeMap<String,String> entradas;
	public static String id;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		Calendar data = Calendar.getInstance();
		id = "Exe" + 
				data.get(Calendar.YEAR) + 
				(data.get(Calendar.MONTH)+1) + 
				data.get(Calendar.DAY_OF_MONTH) +
				data.get(Calendar.HOUR_OF_DAY) +
				data.get(Calendar.MINUTE);
		
		File diretorio = new File(id);
		
		if(!diretorio.isDirectory()) diretorio.mkdir();
		
		id = diretorio + "/";

		log = new LogBuilder(id + "Execucao.log");

		log.write("Iniciando " + id + ":");

		log.write("Obtendo as entradas do sistema a partir de conf.ini.");
		init();

		log.write("Conferindo entradas:");
		for(String key : entradas.keySet()){
			log.write(" - " + key + ": " + entradas.get(key));
		}

		log.write("Iniciando fase 1 - Avalia��o dos classificadores...");
		Tabulating.run(id, log, entradas);

		log.write("Finalizando experimento.");

		log.close();

	}

	public static void init(){

		File conf = new File("conf.ini");
		Scanner leitor = null;

		try {
			leitor = new Scanner(conf);
		} catch (FileNotFoundException e) {
			log.write("Falha ao receber as entradas do sistema: " + e.getMessage());
			System.exit(0);
		}

		entradas = new TreeMap<String,String>();

		while(leitor.hasNextLine()){
			String linha = leitor.nextLine();
			String parametros[] = linha.split("=");
			if(parametros.length < 2) continue;
			entradas.put(parametros[0], parametros[1]);
		}

		log.write("Entradas obtidas.");
		leitor.close();
	}

}
