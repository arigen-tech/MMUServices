package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DgNormalValueDao;
import com.mmu.services.entity.DgNormalValue;


@Repository
@Transactional
public class DgNormalValueDaoImpl extends GenericDaoImpl<DgNormalValue, Long>  implements DgNormalValueDao{

}
