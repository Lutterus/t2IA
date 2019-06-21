package abc.abcde;

import java.io.IOException;

public class App {
	public static void main(String[] args) {
		Storage storage = new Storage();

		// lendo o arquivo xlsx e agrupando todo conteudo em um array
		try {
			storage.loadFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// distribuicao do conteudo do array, criando um array para cada pergunta individualmente
		storage.createStorageReference();

		// normalizacao das palavras
		storage.completeNormalization();
	}

}
