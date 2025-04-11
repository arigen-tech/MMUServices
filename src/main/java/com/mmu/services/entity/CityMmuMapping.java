package com.mmu.services.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "city_mmu_mapping")
@SequenceGenerator(name="CITY_MMU_MAPPING_SEQ", sequenceName="city_mmu_mapping_seq", allocationSize=1)
public class CityMmuMapping implements Serializable {

    private static final long serialVersionUID = 984527268265139443L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="CITY_MMU_MAPPING_SEQ")
    @Column(name = "city_mmu_mapping_id")
    private Long cityMmuMappingId;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "mmu_id")
    private Long mmuId;

    @Column(name = "status")
    private String status;

    @Column(name = "last_chg_by")
    private Long lastChangeBy;

    @Column(name = "last_chg_date")
    private Date lastChangeDate;
    
    //bi-directional many-to-one association to Patient
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="city_id",nullable=false,insertable=false,updatable=false)
    @JsonBackReference
    private MasCity masCity;

    //bi-directional many-to-one association to Patient
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mmu_id",nullable=false,insertable=false,updatable=false)
    @JsonBackReference
    private MasMMU masMmu;

    public Long getCityMmuMappingId() {
        return cityMmuMappingId;
    }

    public void setCityMmuMappingId(Long cityMmuMappingId) {
        this.cityMmuMappingId = cityMmuMappingId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getMmuId() {
        return mmuId;
    }

    public void setMmuId(Long mmuId) {
        this.mmuId = mmuId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLastChangeBy() {
        return lastChangeBy;
    }

    public void setLastChangeBy(Long lastChangeBy) {
        this.lastChangeBy = lastChangeBy;
    }

    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

	public MasCity getMasCity() {
		return masCity;
	}

	public void setMasCity(MasCity masCity) {
		this.masCity = masCity;
	}

	public MasMMU getMasMmu() {
		return masMmu;
	}

	public void setMasMmu(MasMMU masMmu) {
		this.masMmu = masMmu;
	}
    
	public static final Comparator<CityMmuMapping> orderNoComparator = new Comparator<CityMmuMapping>() {
	    public int compare(CityMmuMapping o1, CityMmuMapping o2) {
	    	  
	    	 String o1StringPart =o1.getMasMmu().getMmuName().replaceAll("\\d", "");
             String o2StringPart = o2.getMasMmu().getMmuName().replaceAll("\\d", "");


             if(o1StringPart.equalsIgnoreCase(o2StringPart))
             {
                 return extractInt(o1.getMasMmu().getMmuName()) - extractInt(o2.getMasMmu().getMmuName());
             }
             return o1.getMasMmu().getMmuName().compareTo(o2.getMasMmu().getMmuName());
             
	    	//return extractInt(o1.getMmuName()) - extractInt(o2.getMmuName());
	    } 
	     int extractInt(String s) {
	    	
		   String num = s.replaceAll("\\D", "");
	        // return 0 if no digits found
	        return num.isEmpty() ? 0 : Integer.parseInt(num);
	    }
	  };
 
		   	  
}
