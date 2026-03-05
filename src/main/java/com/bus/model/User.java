package com.bus.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String contact;
  private String email;
  public Long getUserId() {
	return userId;
  }
  public void setUserId(Long userId) {
	this.userId = userId;
  }
  public String getUsername() {
	return username;
  }
  public void setUsername(String username) {
	this.username = username;
  }
  public String getPassword() {
	return password;
  }
  public void setPassword(String password) {
	this.password = password;
  }
  public String getFirstName() {
	return firstName;
  }
  public void setFirstName(String firstName) {
	this.firstName = firstName;
  }
  public String getLastName() {
	return lastName;
  }
  public void setLastName(String lastName) {
	this.lastName = lastName;
  }
  public String getContact() {
	return contact;
  }
  public void setContact(String contact) {
	this.contact = contact;
  }
  public String getEmail() {
	return email;
  }
  public void setEmail(String email) {
	this.email = email;
  }
  
}


