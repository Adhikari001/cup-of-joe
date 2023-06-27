package com.example.cupofjoe.repository;

import com.example.cupofjoe.dto.cafe.CafeResponse;
import com.example.cupofjoe.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, String> {
    Optional<MyUser> findByEmail(String email);

    @Query(value = "select new com.example.cupofjoe.dto.cafe.CafeResponse(user.id, user.cafeName) from MyUser user where coalesce(user.cafeName, '') <> '' and user.id!=:userId")
    List<CafeResponse> findCafeName(String userId);
}
