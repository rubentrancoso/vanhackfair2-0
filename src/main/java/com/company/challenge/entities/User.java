package com.company.challenge.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.hibernate.validator.constraints.NotEmpty;

import com.company.challenge.entities.audit.Auditable;
import com.company.challenge.helper.UUIDGen;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(content=Include.NON_NULL, value=Include.NON_NULL)
public class User extends Auditable<User> {

	@Id
	@Column(name = "UUID",unique=true, nullable = false)
	private String id;
	
	@NotEmpty
	@Column(nullable = false)
	private String name;

	@Column(unique=true)
	private String email;
	
	@NotEmpty
	@Column(nullable = false)
	private String password;

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<Phone> phones;

	@JsonFormat(pattern="EEE yyyy-MM-dd HH:mm:ss.SSSZ")
	private Date last_login;
	private String token;
	
	User() {
		generateId();
	}
	
	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	private void generateId() {
		this.id = UUIDGen.getUUID();
	}
	
    @PrePersist
    public void onPersist() {
    	generateId();
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId() {}
	
	public String getId() {
		return id;
	}

	public Set<Phone> getPhones() {
		if(this.phones == null)
			this.phones = new HashSet<Phone>();
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}
	
	public Set<Phone> addPhone(Phone phone) {
		if(this.phones == null)
			this.phones = new HashSet<Phone>();
		this.phones.add(phone);
		return getPhones();
	}
	
	public Set<Phone> removePhone(Phone phone) {
		this.phones.remove(phone);
		return getPhones();
	}

	public Date getLast_login() {
		return last_login;
	}

	public void setLast_login() {
		this.last_login = new Date();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLast_login(Date last_login) {
		this.last_login = last_login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(this.phones!=null)
			for(Phone phone: this.phones) {
				sb.append(String.format("[phone=(%s) %s],", phone.getDdd(), phone.getNumber()));
			}
		String phones = sb.toString();
		return String.format("[uuid=%s] [email=%s] [phones=%s] [created=%tc] [modified=%tc] [last_login=%tcL] [token=%s]", this.id, this.email, phones, this.created, this.modified, this.last_login, this.token);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || !(obj instanceof User))
			return false;
		User user = (User) obj;
		return user.getEmail().equals(this.email);
	}
	
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + email.hashCode();
        return result;
    }	

}
