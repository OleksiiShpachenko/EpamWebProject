package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="category_id")
	private int categoryId;

	@Column(name="category_active")
	private byte categoryActive;

	@Column(name="category_description")
	private String categoryDescription;

	@Column(name="category_name")
	private String categoryName;
	
	@Column(name="category_user_id")
	private int category_user_id;

	//bi-directional many-to-many association to Test
	@ManyToMany(mappedBy="categories")
	private List<Test> tests;

	// bi-directional many-to-many association to Community
		@ManyToMany(mappedBy = "categories")
		private List<Community> communities;
	
	public Category() {
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public byte getCategoryActive() {
		return this.categoryActive;
	}

	public void setCategoryActive(byte categoryActive) {
		this.categoryActive = categoryActive;
	}

	public String getCategoryDescription() {
		return this.categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Test> getTests() {
		return this.tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	public int getCategory_user_id() {
		return category_user_id;
	}

	public void setCategory_user_id(int category_user_id) {
		this.category_user_id = category_user_id;
	}
	public List<Community> getCommunities() {
		return this.communities;
	}

	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryActive;
		result = prime * result + ((categoryDescription == null) ? 0 : categoryDescription.hashCode());
		result = prime * result + categoryId;
		result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
		result = prime * result + category_user_id;
		result = prime * result + ((tests == null) ? 0 : tests.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (categoryActive != other.categoryActive)
			return false;
		if (categoryDescription == null) {
			if (other.categoryDescription != null)
				return false;
		} else if (!categoryDescription.equals(other.categoryDescription))
			return false;
		if (categoryId != other.categoryId)
			return false;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		if (category_user_id != other.category_user_id)
			return false;
		if (tests == null) {
			if (other.tests != null)
				return false;
		} else if (!tests.equals(other.tests))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryActive=" + categoryActive + ", categoryDescription="
				+ categoryDescription + ", categoryName=" + categoryName + ", category_user_id=" + category_user_id
				+ ", tests=" + tests + "]";
	}

	

}