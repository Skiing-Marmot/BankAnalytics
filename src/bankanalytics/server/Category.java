package bankanalytics.server;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import bankanalytics.client.CategoryInfo;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Category {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	@Persistent
	private String categoryName;
	@Persistent
	private String color;
	@Persistent
	private double categorySum;

	/**
	 * @param categoryName
	 * @param color
	 */
	public Category(String categoryName, String color) {
		super();
		this.categoryName = categoryName;
		this.color = color;
		this.categorySum = 0;
	}

	/**
	 * @param categoryName
	 */
	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
		this.color = "white";
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

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the categorySum
	 */
	public double getCategorySum() {
		return categorySum;
	}

	/**
	 * @param categorySum the categorySum to set
	 */
	public void setCategorySum(double categorySum) {
		this.categorySum = categorySum;
	}

	public void addAmountToSum(double amount) {
		this.categorySum += amount;
	}

	public void removeAmountFromSum(double amount) {
		this.categorySum -= amount;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	public CategoryInfo getCategoryInfo() {
		return new CategoryInfo(getId(), getCategoryName(), getColor(), getCategorySum());
	}
}
