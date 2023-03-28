package com.example.demo.repository;

import com.example.demo.security.user.Authority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, String> {
    @Query("select auth from Authority auth where auth.enabled = true order by auth.id limit 1")
    Authority getUserAuthority();
}
