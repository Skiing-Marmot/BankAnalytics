package bankanalytics.server;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import bankanalytics.client.TransactionLineInfo;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TransactionLine implements Comparable<TransactionLine> {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;
	@Persistent
	private Date addDate;
	@Persistent
	private String description;
	@Persistent
	private double amount;
	@Persistent
	private String categoryName;
	
	public TransactionLine() {
		
	}
	
	public TransactionLine(Date date, String description, String category, double amount) {
		this();
		this.addDate = date;
		this.description = description;
		this.amount = amount;
		this.categoryName = category;
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

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getId() {
		return id;
	}

	public Date getAddDate() {
		return addDate;
	}
	
	public TransactionLineInfo getTransactionLineInfo() {
		return new TransactionLineInfo(id, getAddDate(), getDescription(), categoryName, getAmount(), 0);
	}

	/*
	 * Used to sort transactions by date.
	 */
	@Override
	public int compareTo(TransactionLine o) {
		return this.addDate.compareTo(o.addDate);
	}
}
