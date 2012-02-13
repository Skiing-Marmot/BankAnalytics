package bankanalytics.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import bankanalytics.client.AccountInfo;
import bankanalytics.client.CategoryInfo;
import bankanalytics.client.TransactionLineInfo;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Account {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String accountName;
	@Persistent
	private User user;
	@Persistent
	private List<Category> categories = new ArrayList<Category>();
	@Persistent
	private List<TransactionLine> statements = new ArrayList<TransactionLine>();
	@Persistent
	private double runningBalance;
	
	/**
	 * @param accountName
	 * @param user
	 */
	public Account(String accountName, User user) {
		super();
		this.accountName = accountName;
		this.user = user;
		this.runningBalance = 0;
		//this.categories = new ArrayList<Category>();
		//this.statements = new ArrayList<TransactionLine>();
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}
	
	public void addCategory(Category category) {
		this.categories.add(category);
	}
	
	public void removeCategory(Category category) {
		this.categories.remove(category);
	}

	/**
	 * @return the statements
	 */
	public List<TransactionLine> getStatements() {
		return statements;
	}
	
	public void addStatement(TransactionLine statement) {
		this.statements.add(statement);
	}
	
	public void removeStatement(TransactionLine statement) {
		this.statements.remove(statement);
	}
	
	private List<TransactionLineInfo> getTransactionLineInfos() {
		List<TransactionLineInfo> liste = new ArrayList<TransactionLineInfo>();
		for(TransactionLine t : statements) {
			liste.add(t.getTransactionLineInfo());
		}
		return liste;
	}
	
	private List<CategoryInfo> getCategoryInfos() {
		List<CategoryInfo> liste = new ArrayList<CategoryInfo>();
		for(Category c : categories) {
			liste.add(c.getCategoryInfo());
		}
		return liste;
	}
	
	public AccountInfo getAccountInfo() {
		return new AccountInfo(getId(), getAccountName(), getCategoryInfos(), getTransactionLineInfos(), getRunningBalance());
	}
}
