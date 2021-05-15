package com.dao;

import com.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByEmailAndPassword(String email, String password);

        //Query
//    @Query("SELECT con FROM Users con  WHERE con.phoneType=(:pType) AND con.lastName= (:lName)")
//    List<Users> findByLastNameAndPhoneType(@Param("pType") PhoneType pType, @Param("lName") String lName);

        //Query - Collection Paramater
//    @Query(value = "SELECT u FROM User u WHERE u.name IN :names")
//    List<User> findUserByNameList(@Param("names") Collection<String> names);

}
