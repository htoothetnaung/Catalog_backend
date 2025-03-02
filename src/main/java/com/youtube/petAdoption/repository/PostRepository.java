package com.youtube.petAdoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youtube.petAdoption.enums.Breed;
import com.youtube.petAdoption.enums.Gender;
import com.youtube.petAdoption.enums.Size;
import com.youtube.petAdoption.enums.Type;
import com.youtube.petAdoption.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
	 List<Post> findByType(Type type);
	 @Query("SELECT p FROM Post p WHERE p.type = :type " +
		       "AND (:breed IS NULL OR p.breed = :breed) " +
		       "AND (:age IS NULL OR p.age = :age) " +
		       "AND (:size IS NULL OR p.size = :size) " +
		       "AND (:gender IS NULL OR p.gender = :gender) " +
		       "AND (:color IS NULL OR p.color = :color)")
		List<Post> findByFilters(
		    @Param("type") Type type,
		    @Param("breed") Breed breed,
		    @Param("age") Integer age,
		    @Param("size") Size size,
		    @Param("gender") Gender gender,
		    @Param("color") String color
		);

	
}

