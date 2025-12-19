package com.hustleborn.service.model.users;

import java.time.LocalDate;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.Transient;

import com.hustleborn.service.model.userprofiles.UserProfiles;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String displayName;

	@Column(unique = true, nullable = false)
	private String username;

	private String password;

	private LocalDate lastLoginAt;

	private LocalDate createdAt;

	private LocalDate updatedAt;

	private String modifiedBy;

	private Boolean isEnabled;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	private Map<String, Object> preferences;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private UserProfiles userProfiles;

	@Transient
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getEnabled() {
		return isEnabled;
	}

	public void setEnabled(Boolean enabled) {
		isEnabled = enabled;
	}

	public LocalDate getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(LocalDate lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, Object> getPreferences() {
		return preferences;
	}

	public void setPreferences(Map<String, Object> preferences) {
		this.preferences = preferences;
	}

	public UserProfiles getUserProfile() {
		return userProfiles;
	}

	public void setUserProfile(UserProfiles userProfiles) {
		this.userProfiles = userProfiles;
	}

	public String getFcmToken() {
		if (preferences == null)
			return null;
		Object token = preferences.get("fcmToken");
		return token != null ? token.toString() : null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
