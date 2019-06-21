package abc.abcde;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Storage {
	private ArrayList<StorageObj> storage;
	private Iterator<Row> rowIt = null;
	private Crawler crawler;

	public Storage() {
		storage = new ArrayList<StorageObj>();
		crawler = new Crawler();
	}

	public void completeNormalization() {
		//especificNorma();
		storage.remove(0);
		int current = 0;
		for (StorageObj storageObj : storage) {
			System.out.println("ATUAL: " + current);
			System.out.println("pergunta: " + storageObj.getQuesion());
			crawler.normalizating(storageObj);
			storageObj.myToString();
			storeData(storageObj);
			current++;
		}

	}

	public void especificNorma() {
		storage.remove(0);
		int current = 432;
		for (int i = 0; i < 10; i++) {
			System.out.println("ATUAL: " + current);
			System.out.println("pergunta: " + storage.get(current).getQuesion());
			crawler.normalizating(storage.get(current));
			storage.get(current).myToString();
			storeData(storage.get(current));
			current++;
		}

	}

	public Iterator<Row> loadFile() throws IOException {
		File excelFile = new File("src/storage.xlsx");
		FileInputStream fis = new FileInputStream(excelFile);

		// we create an XSSF Workbook object for our XLSX Excel File
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		// we get first sheet
		XSSFSheet sheet = workbook.getSheetAt(0);

		// we iterate on rows
		rowIt = sheet.iterator();

		workbook.close();
		fis.close();

		return rowIt;

	}

	public void createStorageReference() {
		while (rowIt.hasNext()) {
			Row row = rowIt.next();

			// iterate on cells for the current row
			Iterator<Cell> cellIterator = row.cellIterator();
			StorageObj newObj = new StorageObj();
			int i = 0;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (i == 0) {
					newObj.setQuesion(cell.toString());
				} else if (i == 1) {
					newObj.setAnswer(cell.toString());
				} else if (i == 2) {
					newObj.setClas(cell.toString());
				}
				i++;
			}

			storage.add(newObj);
		}

	}

	private void storeData(StorageObj storageObj) {
		try {
			File file = new File("src/data.txt");

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);

			String content = storageObj.getQuesion();
			bw.write(content);
			bw.write(System.getProperty("line.separator"));

			content = storageObj.getAnswer();
			bw.write(content);
			bw.write(System.getProperty("line.separator"));

			content = storageObj.getClas();
			bw.write(content);
			bw.write(System.getProperty("line.separator"));

			content = "";
			for (String string : storageObj.getSplitQuestion()) {
				content = content + string + ",";
			}
			bw.write(content);
			bw.write(System.getProperty("line.separator"));

			content = "";
			for (String string : storageObj.getSplitQuestionNormalized()) {
				content = content + string + ",";
			}
			bw.write(content);
			bw.write(System.getProperty("line.separator"));

			content = "";
			for (String string : storageObj.getSplitQuestionKind()) {
				content = content + string + ",";
			}
			bw.write(content);
			bw.write(System.getProperty("line.separator"));

			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void myToString() {
		for (StorageObj storageObj : storage) {
			System.out.println("Pergunta: " + storageObj.getQuesion());
			System.out.println("Resposta: " + storageObj.getAnswer());
			System.out.println("Classe: " + storageObj.getClas());
			System.out.println("-------------------------");
		}
	}
}
