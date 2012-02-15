package bankanalytics.server;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import bankanalytics.client.NotLoggedInException;

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FileUploadServlet.class.getName());
	private AccountServiceImpl accountService = new AccountServiceImpl();

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			ServletFileUpload upload = new ServletFileUpload();
			res.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					log.warning("Got a form field: " + item.getFieldName() + " " + req.getAttribute("newName"));
				} else {
					log.warning("Got an uploaded file: " + item.getFieldName() + ", name = " + item.getName());

					int len;
					byte[] buffer = new byte[8192];
					while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
						// res.getOutputStream().write(buffer, 0, len);
					}
					String csvFileContent = new String(buffer);
					String name = item.getName().substring(0, item.getName().length() - 4);
					log.warning("Name: " + name);
					// Add the account to the base
					String accountName = createAccount(name);
					if (accountName != null) {
						parseCSVfile(csvFileContent, accountName);
						res.getOutputStream().write(accountName.getBytes());
					}
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	/*
	 * Create the transaction lines parsing the csv file
	 */
	private void parseCSVfile(String csvFileContent, String accountName) {
		String[] csvLines = csvFileContent.split("\n");
		double balance = 0;
		for (int i = 0; i < csvLines.length - 1; i++) {
			String[] lineFields = csvLines[i].split(",");
			// TODO constants for field numbers
			
			String stringDate = lineFields[0];
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			try {
				date = df.parse(stringDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String stringDescription = lineFields[1];
			
			String stringCategory = lineFields[2];
			
			String stringBalance = lineFields[3];
			double lineBalance = Double.parseDouble(stringBalance);
			double amount = lineBalance - balance;
			balance = lineBalance;

			// The new transaction
			TransactionLine tl = new TransactionLine(date, stringDescription, stringCategory, amount);
			try {
				accountService.addCategory(stringCategory, accountName);
				accountService.addTransactionLine(tl, accountName);
			} catch (NotLoggedInException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log.warning("Length: " + csvLines.length + " - Length: "
					+ lineFields.length + " - Date: " + stringDate
					+ " - Description: " + stringDescription + " - Category: "
					+ stringCategory + " - Balance: " + stringBalance);
		}
	}

	/*
	 * Add the new account to the base
	 */
	private String createAccount(String name) {
		try {
			String accountName = accountService.addAccount(name);
			return accountName;
		} catch (NotLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}