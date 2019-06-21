package abc.abcde;

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

	public StorageObj normalizating(StorageObj storageObj) {
		typeText(storageObj.getQuesion());
		clickButton();
		getResult(storageObj);
		return storageObj;
	}

	private void configureTextField() {
		text = page.getElementByName("text");
	}

	private void configureHtmlPage() {
		try {
			page = wb.getPage("https://visl.sdu.dk/visl/pt/parsing/automatic/parse.php");
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

		for (int i = 8; i < str_array.length - 1; i++) {
			String[] word = str_array[i].split(" ");
			
			if (word.length > 3) {
				int position = 0;
				while (word[position].contains("[") == false) {
					if(word[position].contains("ALT")==false && word[position].contains("xxx")==false) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void typeText(String string) {
		text.setText(string);

	}
}
