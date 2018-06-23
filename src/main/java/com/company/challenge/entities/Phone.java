package com.company.challenge.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Phone {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer ddd;
	private Integer number;

	Phone() {
	}

	public Phone(Integer ddd, Integer number) {
		this.ddd = ddd;
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDdd() {
		return ddd;
	}

	public void setDdd(Integer ddd) {
		this.ddd = ddd;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String toString() {
		return String.format("[id=%d] [ddd=%s] [number=%s]", this.id, this.ddd, this.number);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || !(obj instanceof Phone))
			return false;
		Phone phone = (Phone) obj;
		return phone.ddd.equals(this.ddd) && phone.number.equals(this.number);
	}
	
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + ddd.hashCode();
        result = 31 * result + number.hashCode();
        return result;
    }	
}