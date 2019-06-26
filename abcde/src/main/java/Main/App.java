package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Bow.BoW;
import Storage.Storage;
import Weka.Weka;

public class App {
	private static Storage storage;

	public static void main(String[] args) {
		storage = new Storage();
		getUserOption1();
	}

	private static void getUserOption2(Scanner scan, Weka weka, BoW bow) {

		System.out.println("O que você deseja fazer agora?");
		String input = "a";
		while (input.contentEquals("1") == false && input.contentEquals("2") == false
				&& input.contentEquals("3") == false) {
			System.out.println("1 - Realizar os testes usando a GUI do Weka (encerrar o programa)");
			System.out.println("2 - Realizar os testes automaticamente");
			System.out.println("3 - Avaliar uma pergunta especifica");
			input = scan.next();
		}

		if (input.contentEquals("2")) {
			try {
				weka.test("teste");
			} catch (Exception e) {
				e.printStackTrace();
			}
			getUserOption2(scan, weka, bow);

		} else if (input.contentEquals("3")) {
			String question;
			System.out.println("digite a pergunta a ser adicionada");
			question = scan.nextLine();
			question = scan.nextLine();
			
			System.out.println("digite a classe da pergunta");
			String clas = scan.nextLine();
			
			System.out.println();
			System.out.println("Iniciando normalização");
			
			weka.createTempStruct(bow, question, clas, storage);

			try {
				weka.createArffTemp();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				weka.test("temp");
			} catch (Exception e) {
				e.printStackTrace();
			}

			getUserOption2(scan, weka, bow);
		}

	}

	private static void getUserOption1() {
		Scanner scan = new Scanner(System.in);
		String input = "abc";
		System.out.println("Olá");
		while (input.contentEquals("1") == false && input.contentEquals("2") == false
				&& input.contentEquals("0") == false) {
			System.out.println("0 - Sair");
			System.out.println(
					"1 - Para carregar do arquivo storage.xlsx e refazer a normatização completa (demorará +10 min)");
			System.out.println("2 - Para carregar do arquivo storage.txt e usar a normatização feita pela ultima vez");
			input = scan.next();
		}

		if (input.contentEquals("0") == false) {
			System.out.println("Iniciando processamento do arquivo");
			if (input.contentEquals("1")) {
				fromExcel();
			} else if (input.contentEquals("2")) {
				fromTxt();
			}
			System.out.println("pronto!");

			BoW bow = new BoW();
			bowCreation(bow, storage);

			Weka weka = new Weka();
			wekaCreation(weka, bow);

			getUserOption2(scan, weka, bow);
		}

	}

	private static void wekaCreation(Weka weka, BoW bow) {
		System.out.println("Criando estruturação para o Weka....");

		weka.createStruct(bow);

		try {
			weka.createArffTestandTraining();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Estrutura pronta para o Weka!!!");

	}

	private static void bowCreation(BoW bow, Storage storage) {
		System.out.println("Agora vamos criar a BoW....");

		bow.splitByClass(storage.getStorageObjList());

		ArrayList<String> rvTerms = new ArrayList<String>();
		rvTerms.add("V");
		rvTerms.add("N");
		rvTerms.add("ADV");
		rvTerms.add("ADJ");
		rvTerms.add("PROP");

		bow.addRelevantTerms(rvTerms);
		bow.cleanIrrelevantTerms();

		int k = 30;
		bow.countTerms();
		bow.setTerms(k);

		System.out.println("BoW criada com sucesso....");

	}

	private static void fromTxt() {
		// lendo o arquivo txt e distribuindo o conteudo em arrays
		try {
			storage.loadFileFromTxt();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void fromExcel() {
		// lendo o arquivo xlsx e agrupando todo conteudo em um array
		try {
			storage.loadFileFromExcel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Iniciado processo de normatização");
		// distribuicao do conteudo do array, criando um array para cada pergunta
		// individualmente
		storage.createStorageReference();

		// normalizacao das palavras
		storage.completeNormalization();

	}

}
