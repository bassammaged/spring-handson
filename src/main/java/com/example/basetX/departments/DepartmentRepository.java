package com.example.basetX.departments;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class DepartmentRepository {

    // Construction Injection for Entity Manager
    private final EntityManager entityManager;

    DepartmentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    
    
    @Transactional
    void create(DepartmentsEntity departmentsEntity) {
        this.entityManager.persist(departmentsEntity);
    }

    DepartmentsEntity findById(int departmentId){
        return this.entityManager.find(DepartmentsEntity.class,departmentId);
    }

    List<DepartmentsEntity> findAll(){
        return this.entityManager.createQuery("SELECT d from DepartmentsEntity d",DepartmentsEntity.class).getResultList();
    }

    @Transactional
    Boolean deleteByCode(String departmentCode) {
        String hql = "DELETE FROM DepartmentsEntity d WHERE d.code = :code";
        Query query = entityManager.createQuery(hql);
        query.setParameter("code", departmentCode);
        int rowsDeleted = query.executeUpdate();
        return rowsDeleted > 0;
    }

    @Transactional
    Boolean updateById(int id, String name, String departmentCode) {
        Optional<DepartmentsEntity> departmentOptional = Optional.ofNullable(this.findById(id));

        if(departmentOptional.isPresent()) {
            DepartmentsEntity department = departmentOptional.get();
            if (name != null && !name.isEmpty()) {
                department.setName(name);
            }
            if (departmentCode != null && !departmentCode.isEmpty()) {
                department.setCode(departmentCode);
            }
            this.entityManager.merge(department);
            return true;
        } else {
            return false;
        }
    }

}
