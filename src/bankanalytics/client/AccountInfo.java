package bankanalytics.client;

import java.io.Serializable;
import java.util.List;

public class AccountInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String accountName;
	private List<CategoryInfo> categories;
	private List<TransactionLineInfo> statements;
	private double runningBalance;

	public AccountInfo() {

	}

	/**
	 * @param id
	 * @param accountName
	 * @param user
	 * @param categories
	 * @param statements
	 * @param runningBalance
	 */
	public AccountInfo(Long id, String accountName,
			List<CategoryInfo> categories,
			List<TransactionLineInfo> statements, double runningBalance) {
		super();
		this.id = id;
		this.accountName = accountName;
		this.categories = categories;
		this.statements = statements;
		this.runningBalance = runningBalance;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the runningBalance
	 */
	public double getRunningBalance() {
		return runningBalance;
	}

	/**
	 * @param runningBalance the runningBalance to set
	 */
	public void setRunningBalance(double runningBalance) {
		this.runningBalance = runningBalance;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the categories
	 */
	public List<CategoryInfo> getCategories() {
		return categories;
	}

	/*
	 * Add category to the categories list
	 */
	public void addCategory(CategoryInfo category) {
		this.categories.add(category);
	}

	/*
	 * Remove category from the list
	 */
	public void removeCategory(CategoryInfo category) {
		this.categories.remove(category);
	}

	/**
	 * @return the statements
	 */
	public List<TransactionLineInfo> getStatements() {
		return statements;
	}

	/*
	 * Add a transaction to the transaction list
	 */
	public void addStatement(TransactionLineInfo transaction) {
		this.statements.add(transaction);
	}

	/*
	 * Remove a transaction from the list
	 */
	public void removeStatement(TransactionLineInfo transaction) {
		this.statements.remove(transaction);
	}

}
