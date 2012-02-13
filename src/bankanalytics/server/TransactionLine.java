package bankanalytics.server;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import bankanalytics.client.CategoryInfo;
import bankanalytics.client.TransactionLineInfo;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TransactionLine {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
//	private Long id;
	private String id;
	@Persistent
	private Date addDate;
	@Persistent
	private String description;
	@Persistent
	private double amount;
	@Persistent
	private Category category;
//	@Persistent
//	private String category;
//	private static double currentRunningBalance = 0; // Account running balance.
	@Persistent
	private double lineBalance; // balance just after that transaction line was added.
	
	public TransactionLine() {
		this.addDate = new Date();
	}
	
	public TransactionLine(String description, Category category, double amount, double lineBalance) {
		this();
		this.description = description;
		this.amount = amount;
		this.category = category;
		this.lineBalance = lineBalance;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
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
	
	public TransactionLineInfo getTransactionLineInfo() {
		CategoryInfo catInfo = this.category.getCategoryInfo();
		return new TransactionLineInfo(id, getAddDate(), getDescription(), catInfo, getAmount(), getLineBalance());
	}
}
