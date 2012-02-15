package bankanalytics.client;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.Window;

public class TransactionLineInfo implements Serializable, Comparable<TransactionLineInfo> {
	
	private static final long serialVersionUID = 1L;

	private String id;

	private Date addDate;

	private String description;

	private double amount;

	private String category;

	private double lineBalance; // balance just after that transaction line was added.
	
	public TransactionLineInfo() {
	}
	
	public TransactionLineInfo(String id, Date addDate, String description, String category, double amount, double lineBalance) {
		this();
		this.id = id;
		this.addDate = addDate;
		this.description = description;
		this.amount = amount;
		this.category = category;
		this.lineBalance = lineBalance;
//		TransactionLine.currentRunningBalance += amount;
//		this.lineBalance = TransactionLine.getCurrentRunningBalance();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

//	public static double getCurrentRunningBalance() {
//		return currentRunningBalance;
//	}
//
//	public static void setCurrentRunningBalance(double currentRunningBalance) {
//		TransactionLine.currentRunningBalance = currentRunningBalance;
//	}

	public double getLineBalance() {
		return lineBalance;
	}

	public void setLineBalance(double lineBalance) {
		this.lineBalance = lineBalance;
	}

	public String getId() {
		return id;
	}

	public Date getAddDate() {
		return addDate;
	}

	/**
	 * @param addDate the addDate to set
	 */
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	@Override
	public int compareTo(TransactionLineInfo o) {
		return this.addDate.compareTo(o.addDate);
	}
}
