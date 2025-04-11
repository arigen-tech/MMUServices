/*
 * package com.mmu.services.entity;
 * 
 * import java.io.Serializable;
 * 
 * import javax.persistence.Column; import javax.persistence.Entity; import
 * javax.persistence.FetchType; import javax.persistence.GeneratedValue; import
 * javax.persistence.GenerationType; import javax.persistence.Id; import
 * javax.persistence.JoinColumn; import javax.persistence.ManyToOne; import
 * javax.persistence.Table;
 * 
 * import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 * 
 * @Entity
 * 
 * @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
 * 
 * @Table(name = "mas_city") public class MasCampDepartment implements
 * Serializable {
 * 
 * private static final long serialVersionUID = 1471210576217142696L;
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.AUTO, generator="")
 * 
 * @Column(name = "camp_department_id") private Long campDepartmentId;
 * 
 * @ManyToOne(fetch = FetchType.LAZY)
 * 
 * @JoinColumn(name = "department_id") private MasDepartment masDepartment;
 * 
 * @ManyToOne(fetch = FetchType.LAZY)
 * 
 * @JoinColumn(name = "camp_id") private MasCamp masCamp;
 * 
 * public MasCampDepartment() { super(); }
 * 
 * public Long getCampDepartmentId() { return campDepartmentId; }
 * 
 * public void setCampDepartmentId(Long campDepartmentId) {
 * this.campDepartmentId = campDepartmentId; }
 * 
 * public MasDepartment getMasDepartment() { return masDepartment; }
 * 
 * public void setMasDepartment(MasDepartment masDepartment) {
 * this.masDepartment = masDepartment; }
 * 
 * public MasCamp getMasCamp() { return masCamp; }
 * 
 * public void setMasCamp(MasCamp masCamp) { this.masCamp = masCamp; }
 * 
 * }
 */