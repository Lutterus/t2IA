package Crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

import Storage.StorageObj;

public class Crawler {
	private WebClient wb;
	private HtmlPage page;
	private HtmlTextArea text;
	private HtmlPage pageButton;

	public Crawler() {
		configureWebClient();
		configureHtmlPage();
		configureTextField();
	}

	public StorageObj normalizating(StorageObj storageObj, int count) {
		StorageObj temp;
		typeText(storageObj.getQuesion());
		clickButton();
		temp = getResult(storageObj);

		if (temp != null) {
			return temp;
		} else {
			System.out.println("erro ao construir a normalização... tentando novamente");
			typeText(storageObj.getQuesion());
			clickButton();
			temp = getResult(storageObj);
			if (temp != null) {
				return temp;
			}
			return null;
		}

	}

	private void configureTextField() {
		text = page.getElementByName("text");
	}

	private void configureHtmlPage() {
		try {
			page = wb.getPage("https://visl.sdu.dk/visl/pt/parsing/automatic/parse.php");
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void configureWebClient() {
		wb = new WebClient(BrowserVersion.CHROME);
		wb.getOptions().setJavaScriptEnabled(true);
		wb.getOptions().setThrowExceptionOnFailingStatusCode(false);
		wb.getOptions().setThrowExceptionOnScriptError(false);

	}

	private StorageObj getResult(StorageObj storageObj) {
		List<HtmlTableDataCell> profileDivLinks = pageButton.getByXPath("//tr//td");
		HtmlTableDataCell area = profileDivLinks.get(12);
		String echo = area.asText();
		String[] str_array = echo.split("\n");

		for (int i = 8; i < str_array.length; i++) {
			if (str_array[i].contains("The requested string:") || str_array[i].contains("No sentence entered")
					|| str_array[i].contentEquals(" ")) {
				return null;
			}
		}

		for (int i = 8; i < str_array.length - 1; i++) {
			String[] word = str_array[i].split(" ");

			if (word.length > 3) {
				int position = 0;
				while (word[position].contains("[") == false) {
					if (word[position].contentEquals("ALT") == false && word[position].contains("xx") == false) {
						storageObj.addSplitQuestion(word[position]);
					}
					position++;
				}
				storageObj.addQuestionNormalized(word[position]);
				position++;

				while (word[position].contains(">") == true) {
					position++;
				}

				storageObj.addSplitQuestionKind(word[position]);
			}

		}

		return storageObj;

	}

	private void clickButton() {
		List<Object> listButton = page
				.getByXPath("//html//body//table//tbody//tr//td//form//input[@type='submit' and @value='Go!']");

		HtmlSubmitInput submitBt = (HtmlSubmitInput) listButton.get(0);
		try {
			pageButton = submitBt.click();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void typeText(String string) {
		text.setText(string);
	}
}
