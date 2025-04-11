package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.MasAppointmentTypeDao;
import com.mmu.services.entity.MasAppointmentType;

@Repository
@Transactional
public class MasAppointmentTypeDaoImpl extends GenericDaoImpl<MasAppointmentType, Long>  implements MasAppointmentTypeDao {

}
