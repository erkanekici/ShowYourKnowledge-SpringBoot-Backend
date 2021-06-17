package com;

import com.dao.UserInfoRepository;
import com.entity.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
//@Sql({"/data.sql"})
//@Sql({"/data.sql" , "/import.sql"})
//@Sql(statements = {
//       "INSERT INTO ..."
//})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UserTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Before
    public void setup() {
        System.out.println("Test setup");
    }

    @Test
    @DisplayName("create-user-test")
    @Order(1)
    void createUserTest(){
        System.out.println("Test Start");
        UserInfo user = userInfoRepository.findByEmailAndPassword("email","123").get();
        Assert.assertTrue(user.getUserName().equals("Erkan"));
    }


}
