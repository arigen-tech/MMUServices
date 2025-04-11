package com.mmu.services.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the mas_mmu database table.
 * 
 */
@Entity
@Table(name="mas_mmu")
@NamedQuery(name="MasMMU.findAll", query="SELECT m FROM MasMMU m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MMU_GENERATOR", sequenceName="mas_bus_seq", allocationSize=1)
public class MasMMU implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator ="MAS_MMU_GENERATOR")
	@Column(name="mmu_id")
	private Long mmuId;

	@Column(name="city_id")
	private Long cityId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mmu_code")
	private String mmuCode;

	@Column(name="mmu_name")
	private String mmuName;

	@Column(name="mmu_no")
	private String mmuNo;

	@Column(name="mmu_type_id")
	private Long mmuTypeId;

	@Column(name="mmu_vendor_id")
	private Long mmuVendorId;

	@Column(name="oprational_date")
	private Date oprationalDate;

	private String status;
	
	@Column(name="map_id")
	private Long officeId;
	
	@Column(name="poll_no")
	private String pollNo;
	
	@Column(name="chassis_no")
	private String chassisNo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "city_id",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasCity masCity;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "mmu_vendor_id",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasMmuVendor masMmuVendor;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "mmu_type_id",nullable=false,insertable=false,updatable=false)
	@JsonBackReference
	private MasMmuType MasMmuType;

	public MasMMU() {
	}

	public Long getMmuId() {
		return this.mmuId;
	}

	public void setMmuId(Long mmuId) {
		this.mmuId = mmuId;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getLastChgBy() {
		return this.lastChgBy;
	}

	public void setLastChgBy(Long lastChgBy) {
		this.lastChgBy = lastChgBy;
	}

	public Timestamp getLastChgDate() {
		return this.lastChgDate;
	}

	public void setLastChgDate(Timestamp lastChgDate) {
		this.lastChgDate = lastChgDate;
	}

	public String getMmuCode() {
		return this.mmuCode;
	}

	public void setMmuCode(String mmuCode) {
		this.mmuCode = mmuCode;
	}

	public String getMmuName() {
		return this.mmuName;
	}

	public void setMmuName(String mmuName) {
		this.mmuName = mmuName;
	}

	public String getMmuNo() {
		return this.mmuNo;
	}

	public void setMmuNo(String mmuNo) {
		this.mmuNo = mmuNo;
	}

	public Long getMmuTypeId() {
		return this.mmuTypeId;
	}

	public void setMmuTypeId(Long mmuTypeId) {
		this.mmuTypeId = mmuTypeId;
	}

	public Long getMmuVendorId() {
		return this.mmuVendorId;
	}

	public void setMmuVendorId(Long mmuVendorId) {
		this.mmuVendorId = mmuVendorId;
	}

	public Date getOprationalDate() {
		return this.oprationalDate;
	}

	public void setOprationalDate(Date oprationalDate) {
		this.oprationalDate = oprationalDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public MasMmuVendor getMasMmuVendor() {
		return masMmuVendor;
	}

	public void setMasMmuVendor(MasMmuVendor masMmuVendor) {
		this.masMmuVendor = masMmuVendor;
	}

	public MasMmuType getMasMmuType() {
		return MasMmuType;
	}

	public void setMasMmuType(MasMmuType masMmuType) {
		MasMmuType = masMmuType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LAST_CHG_BY",nullable=false,insertable=false,updatable=false)
	private Users user;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	
	@OneToMany(fetch=FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="ROLE_ID")	
	private List<MasCamp> masCamps;

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getPollNo() {
		return pollNo;
	}

	public void setPollNo(String pollNo) {
		this.pollNo = pollNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}
	
	@Column(name="district_id")
	private Long districtId;

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	
	public static final Comparator<MasMMU> orderNoComparator = new Comparator<MasMMU>() {
	    public int compare(MasMMU o1, MasMMU o2) {
	    	  
	    	 String o1StringPart =o1.getMmuName().replaceAll("\\d", "");
             String o2StringPart = o2.getMmuName().replaceAll("\\d", "");


             if(o1StringPart.equalsIgnoreCase(o2StringPart))
             {
                 return extractInt(o1.getMmuName()) - extractInt(o2.getMmuName());
             }
             return o1.getMmuName().compareTo(o2.getMmuName());
             
	    	//return extractInt(o1.getMmuName()) - extractInt(o2.getMmuName());
	    } 
	     int extractInt(String s) {
	    	
		   String num = s.replaceAll("\\D", "");
	        // return 0 if no digits found
	        return num.isEmpty() ? 0 : Integer.parseInt(num);
	    }
	  };
	
}