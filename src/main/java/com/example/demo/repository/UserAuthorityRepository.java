package com.example.demo.repository;

import com.example.demo.domain.user.UserAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends CrudRepository<UserAuthority, Long> {
    @Override
    <S extends UserAuthority> S save(S entity);
}
