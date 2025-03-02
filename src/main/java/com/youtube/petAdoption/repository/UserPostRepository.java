package com.youtube.petAdoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
//import com.youtube.petAdoption.enums.PetType;

import com.youtube.petAdoption.enums.Type;
import com.youtube.petAdoption.model.UserPost;


@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    List<UserPost> findByUserId(Long userId);
    
    List<UserPost> findByType(String type);


    @Query("SELECT u FROM UserPost u WHERE u.type = :type " +
            "AND (:breed IS NULL OR u.breed LIKE %:breed%) " +
            "AND (:age IS NULL OR u.age = :age) " +
            "AND (:size IS NULL OR u.size LIKE %:size%) " +
            "AND (:gender IS NULL OR u.gender LIKE %:gender%) " +
            "AND (:color IS NULL OR u.color LIKE %:color%)")
     List<UserPost> searchPets(@Param("type") String type,
                               @Param("breed") String breed,
                               @Param("age") Integer age,
                               @Param("size") String size,
                               @Param("gender") String gender,
                               @Param("color") String color);
 
}
