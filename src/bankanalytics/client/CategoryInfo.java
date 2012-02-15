package bankanalytics.client;

import java.io.Serializable;

public class CategoryInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String categoryName;
	private String color; // Color in which transaction of that category will be highlighted (not implemented).
	private double sum;
	
	public CategoryInfo() {
		
	}
	
	/**
	 * @param id
	 * @param categoryName
	 * @param color
	 */
	public CategoryInfo(String id, String categoryName, String color, double sum) {
		super();
		this.id = id;
		this.categoryName = categoryName;
		this.color = color;
		this.sum = sum;
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
	 * @return the sum
	 */
	public double getSum() {
		return sum;
	}

	/**
	 * @param sum the sum to set
	 */
	public void setSum(double sum) {
		this.sum = sum;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
