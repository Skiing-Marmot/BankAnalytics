package bankanalytics.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TransactionLine {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private Date addDate;
	@Persistent
	private String description;
	@Persistent
	private double amount;
//	@Persistent
//	private Category category;
	@Persistent
	private String category;
	private static double currentRunningBalance = 0; // Account running balance.
	@Persistent
	private double lineBalance; // balance just after that transaction line was added.
	
	public TransactionLine() {
		this.addDate = new Date();
	}
	
	public TransactionLine(String description, double amount, String category) {
		this();
		this.description = description;
		this.amount = amount;
		this.category = category;
		TransactionLine.currentRunningBalance += amount;
		this.lineBalance = TransactionLine.getCurrentRunningBalance();
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

	public static double getCurrentRunningBalance() {
		return currentRunningBalance;
	}

	public static void setCurrentRunningBalance(double currentRunningBalance) {
		TransactionLine.currentRunningBalance = currentRunningBalance;
	}

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
