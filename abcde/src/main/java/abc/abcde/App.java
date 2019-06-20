package abc.abcde;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class App {
	public static void main(String[] args) {
		/*Iterator<Row> rowIt = null;
		try {
			rowIt = loadFile(rowIt);
		} catch (IOException e) {
			System.err.println(
					"Nao foi possivel carregar o arquivo xlsx, verifique se ele esta mesmo abaixo da pasta src, dentro do projeto");
			e.printStackTrace();
		}*/

		ArrayList<StorageObj> storage = new ArrayList<StorageObj>();
		//storage = createStorage(rowIt, storage);

		// for (StorageObj storageObj : storage) {
		// System.out.println(storageObj.getQuesion());
		// }

		try {
			storage = normalization(storage);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ArrayList<StorageObj> normalization(ArrayList<StorageObj> storage) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		//Initializin webClient Object to imitate chrome browser
		WebClient wb = new WebClient(BrowserVersion.CHROME);
		wb.getOptions().setJavaScriptEnabled(true);
		wb.getOptions().setThrowExceptionOnFailingStatusCode(false);
		wb.getOptions().setThrowExceptionOnScriptError(false);
		
		HtmlPage page = wb.getPage("https://visl.sdu.dk/visl/pt/parsing/automatic/parse.php");
		
		//Getting form from google home page. tsf is the form name
		HtmlForm form = page.getElementBy
		
		form.getInputByName("text").setValueAttribute("ola");
		
		//Creating a virtual submit button
		HtmlButton submitButton = (HtmlButton)page.createElement("button");
		submitButton.setAttribute("type", "submit");
		form.appendChild(submitButton);
		
		//Submitting the form and getting the result
		HtmlPage newPage = submitButton.click();
		
		//Getting the results as text
		String pageText = newPage.asText();
		System.out.println(pageText);
		return storage;
	}

	private static ArrayList<StorageObj> createStorage(Iterator<Row> rowIt, ArrayList<StorageObj> storage) {
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
		return storage;

	}

	private static Iterator<Row> loadFile(Iterator<Row> rowIt) throws IOException {
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
}
