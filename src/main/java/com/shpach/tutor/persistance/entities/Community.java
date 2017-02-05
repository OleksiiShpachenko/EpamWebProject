package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the community database table.
 * 
 */
@Entity
@NamedQuery(name="Community.findAll", query="SELECT c FROM Community c")
public class Community implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="community_id")
	private int communityId;

	@Column(name="community_active")
	private byte communityActive;

	@Column(name="community_description")
	private String communityDescription;

	@Column(name="community_name")
	private String communityName;

	//bi-directional many-to-many association to Test
	@ManyToMany
	@JoinTable(
		name="test_to_community_relationship"
		, joinColumns={
			@JoinColumn(name="community_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="test_id")
			}
		)
	private List<Test> tests;

	//bi-directional many-to-many association to User
	@ManyToMany
	@JoinTable(
		name="user_to_community_relationship"
		, joinColumns={
			@JoinColumn(name="community_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id")
			}
		)
	private List<User> users;

	public Community() {
	}

	public int getCommunityId() {
		return this.communityId;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	public byte getCommunityActive() {
		return this.communityActive;
	}

	public void setCommunityActive(byte communityActive) {
		this.communityActive = communityActive;
	}

	public String getCommunityDescription() {
		return this.communityDescription;
	}

	public void setCommunityDescription(String communityDescription) {
		this.communityDescription = communityDescription;
	}

	public String getCommunityName() {
		return this.communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public List<Test> getTests() {
		return this.tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + communityActive;
		result = prime * result + ((communityDescription == null) ? 0 : communityDescription.hashCode());
		result = prime * result + communityId;
		result = prime * result + ((communityName == null) ? 0 : communityName.hashCode());
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
		Community other = (Community) obj;
		if (communityActive != other.communityActive)
			return false;
		if (communityDescription == null) {
			if (other.communityDescription != null)
				return false;
		} else if (!communityDescription.equals(other.communityDescription))
			return false;
		if (communityId != other.communityId)
			return false;
		if (communityName == null) {
			if (other.communityName != null)
				return false;
		} else if (!communityName.equals(other.communityName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Community [communityId=" + communityId + ", communityActive=" + communityActive
				+ ", communityDescription=" + communityDescription + ", communityName=" + communityName + "]";
	}

}