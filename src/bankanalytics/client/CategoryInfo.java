package bankanalytics.client;

import java.io.Serializable;

public class CategoryInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String categoryName;
	private String color;
	
	public CategoryInfo() {
		
	}
	
	/**
	 * @param id
	 * @param categoryName
	 * @param color
	 */
	public CategoryInfo(String id, String categoryName, String color) {
		super();
		this.id = id;
		this.categoryName = categoryName;
		this.color = color;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
