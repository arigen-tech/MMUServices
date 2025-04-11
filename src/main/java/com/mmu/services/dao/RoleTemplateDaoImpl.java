package com.mmu.services.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.impl.GenericDaoImpl;
import com.mmu.services.entity.RoleTemplate;

 
@Repository
@Transactional
public class RoleTemplateDaoImpl extends GenericDaoImpl<RoleTemplate, Long> implements RoleTemplateDao{

}
