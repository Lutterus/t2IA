package Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Bow.gramaticalClassList;
import Crawler.Crawler;

public class Storage {
	private ArrayList<StorageObj> storage;
	private Iterator<Row> rowIt = null;
	private Crawler crawler;

	public Storage() {
		storage = new ArrayList<StorageObj>();
		crawler = new Crawler();
	}

	public void completeNormalization() {
		storage.remove(0);
		int current = 0;
		cleanFile();
		for (StorageObj storageObj : storage) {
			System.out.println("linha ATUAL: " + current);
			if (crawler.normalizating(storageObj, 0) == null) {
				System.out.println("nao foi possivel normalizar esta palavra");
			} else {
				// storageObj.myToString();
				storeFreshData(storageObj);
			}
			current++;
		}

	}
	
	public ArrayList<String> oneNormalization(String string, String clas) {
		StorageObj storageObj = new StorageObj();
		storageObj.setQuesion(string);
		storageObj.setClas(clas);

		if (crawler.normalizating(storageObj, 0) == null) {
			return null;
		} else {
			return storageObj.getSplitQuestionNormalized();
		}
	}

	private void cleanFile() {
		File file = new File("./data/data.txt");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.print("");
		writer.close();

	}

	public void loadFileFromTxt() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./data/data.txt"), "UTF8"));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.contentEquals(" ") == false) {
				StorageObj newObj = new StorageObj();
				for (int i = 0; i < 6; i++) {
					if (i == 0) {
						newObj.setQuesion(line);
						line = br.readLine();
					} else if (i == 1) {
						newObj.setAnswer(line);
						line = br.readLine();
					} else if (i == 2) {
						newObj.setClas(line);
						line = br.readLine();
					} else if (i == 3) {
						newObj.setSplitQuestion(line);
						line = br.readLine();
					} else if (i == 4) {
						newObj.setQuestionNormalized(line);
						line = br.readLine();
					} else if (i == 5) {
						newObj.setSplitQuestionKind(line);
					}
				}
				storage.add(newObj);
			}
		}

		br.close();
	}

	public Iterator<Row> loadFileFromExcel() throws IOException {
		File excelFile = new File("./data/storage.xlsx");
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
		int aux = 0;
		while (rowIt.hasNext()) {
			Row row = rowIt.next();
			// iterate on cells for the current row
			Iterator<Cell> cellIterator = row.cellIterator();
			StorageObj newObj = new StorageObj();
			int i = 0;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell.toString().contentEquals(" ") || cell.toString() == null) {
					aux = 1;
				}
				if (i == 0) {
					newObj.setQuesion(cell.toString());
				} else if (i == 1) {
					newObj.setAnswer(cell.toString());
				} else if (i == 2) {
					newObj.setClas(cell.toString());
				}
				i++;
			}
			if (aux == 0) {
				storage.add(newObj);
			} else {
				aux = 0;
			}

		}

	}

	public void storeFreshData(StorageObj storageObj) {
		try {
			File file = new File("./data/data.txt");
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

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<StorageObj> getStorageObjList() {
		return storage;
	}

}
