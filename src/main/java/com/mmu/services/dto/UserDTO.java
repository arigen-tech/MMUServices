package com.mmu.services.dto;

public class UserDTO {
	
	    private Long userId;
	    private String userName;
	    private String mmuId;
	    private String mobileNo;
	    private Long userTypeId;
	    private String userTypeName;
	    private String administrativeSexName;
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getMmuId() {
			return mmuId;
		}
		public void setMmuId(String mmuId) {
			this.mmuId = mmuId;
		}
		public String getMobileNo() {
			return mobileNo;
		}
		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}
		public Long getUserTypeId() {
			return userTypeId;
		}
		public void setUserTypeId(Long userTypeId) {
			this.userTypeId = userTypeId;
		}
		public String getUserTypeName() {
			return userTypeName;
		}
		public void setUserTypeName(String userTypeName) {
			this.userTypeName = userTypeName;
		}
		public String getAdministrativeSexName() {
			return administrativeSexName;
		}
		public void setAdministrativeSexName(String administrativeSexName) {
			this.administrativeSexName = administrativeSexName;
		}
	    
	    

}
