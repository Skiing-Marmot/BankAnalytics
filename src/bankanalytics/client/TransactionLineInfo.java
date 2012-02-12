package bankanalytics.client;

import java.io.Serializable;
import java.util.Date;

public class TransactionLineInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Date addDate;

	private String description;

	private double amount;
//	@Persistent
//	private Category category;

	private String category;
//	@NotPersistent 
//	private static double currentRunningBalance; // Account running balance.

	private double lineBalance; // balance just after that transaction line was added.
	
	public TransactionLineInfo() {
	}
	
	public TransactionLineInfo(long id, Date addDate, String description, double amount, String category, double lineBalance) {
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

	public Long getId() {
		return id;
	}

	public Date getAddDate() {
		return addDate;
	}
}