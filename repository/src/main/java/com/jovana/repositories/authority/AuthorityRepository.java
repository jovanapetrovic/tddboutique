package com.jovana.repositories.authority;

import com.jovana.entity.authority.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by jovana on 24.02.2020
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String>{

    Authority findByName(String name);

    @Query("SELECT a.name FROM User u JOIN u.authorities a WHERE u.id = :userId")
    Authority findByUserId(@Param("userId") Long userId);

}
