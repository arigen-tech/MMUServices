package com.mmu.services.dto;

public class SourceOfMedicine {

	
	public SourceOfMedicine(Long id, String code, String name, Boolean status) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.status = status;
	}


	public SourceOfMedicine() {
	
	}


	private Long id;
	private String code;
	private String name;
	private Boolean status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	
}
