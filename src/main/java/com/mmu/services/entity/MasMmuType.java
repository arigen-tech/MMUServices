package com.mmu.services.entity;
import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;


/**
 * The persistent class for the mas_mmu_type database table.
 * 
 */
@Entity
@Table(name="mas_mmu_type")
@NamedQuery(name="MasMmuType.findAll", query="SELECT m FROM MasMmuType m")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name="MAS_MMU_TYPE_GENERATOR", sequenceName="mas_mmu_type_seq", allocationSize=1)
public class MasMmuType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator ="MAS_MMU_TYPE_GENERATOR")
	@Column(name="mmu_type_id")
	private Long mmuTypeId;

	@Column(name="last_chg_by")
	private Long lastChgBy;

	@Column(name="last_chg_date")
	private Timestamp lastChgDate;

	@Column(name="mmu_type_code")
	private String mmuTypeCode;

	@Column(name="mmu_type_name")
	private String mmuTypeName;

	private String status;

	public MasMmuType() {
	}

	public Long getMmuTypeId() {
		return this.mmuTypeId;
	}

	public void setMmuTypeId(Long mmuTypeId) {
		this.mmuTypeId = mmuTypeId;
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

	public String getMmuTypeCode() {
		return this.mmuTypeCode;
	}

	public void setMmuTypeCode(String mmuTypeCode) {
		this.mmuTypeCode = mmuTypeCode;
	}

	public String getMmuTypeName() {
		return this.mmuTypeName;
	}

	public void setMmuTypeName(String mmuTypeName) {
		this.mmuTypeName = mmuTypeName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}