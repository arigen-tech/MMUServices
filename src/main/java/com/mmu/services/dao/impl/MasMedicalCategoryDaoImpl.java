package com.mmu.services.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.MasMedicalCategoryDao;
import com.mmu.services.entity.MasMedicalCategory;
@Repository
@Transactional
public class MasMedicalCategoryDaoImpl extends GenericDaoImpl<MasMedicalCategory, Long>  implements MasMedicalCategoryDao  {

}
