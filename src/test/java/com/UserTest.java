package com;

import com.dto.UserInfoDTO;
import com.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    /** JUnit NOTES
     import org.junit.Assert; --> JUnit4 Assert (You can use in Junit5)
     import org.junit.runner.RunWith; --> In JUnit4. Use @ExtendWith in Junit5 instead of @RunWith
     import org.springframework.test.context.junit4.SpringRunner; --> Use SpringRunner.class with @RunWith in JUnit4
                                                                      instead of SpringExtension in Junit5.
    */

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        System.out.println("Test Before");
    }

    @Test
    @DisplayName("getUserInfoByEmailAndPassword-test")
    @Order(1)
    @Sql(scripts = "classpath:data/data.sql") //classpath = "src/main/resources/"
    //@Sql({"/data.sql" , "/data2.sql"})
    //@Sql(statements = {
    //       "INSERT INTO ..."
    //})
    public void getUserInfoByEmailAndPassword(){
        System.out.println("Test Started: getUserInfoByEmailAndPassword");

        UserInfoDTO user = userService.getUserInfoByEmailAndPassword("email","123");

        Assertions.assertTrue(user.getUserName().equals("Erkan"));
    }

    @Test
    @DisplayName("getUserInfoByUserRegisterTimeIntervalAndActivity-test")
    @Order(2)
    public void getUserInfoByUserRegisterTimeIntervalAndActivity(){
        System.out.println("Test Started: getUserInfoByUserRegisterTimeIntervalAndActivity");

        //getUserInfoByUserRegisterTimeIntervalAndActivity metodunun registerTimeInterval parametresi "startDate,EndDate" formatındadır.
        List<UserInfoDTO> UserInfoDTOList = userService.getUserInfoByUserRegisterTimeIntervalAndActivity("2011-12-17,2011-12-19",1);

        Assertions.assertTrue(UserInfoDTOList.size() > 0);
    }



}
