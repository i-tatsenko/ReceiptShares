package com.receiptshares.user.registration

import com.receiptshares.user.dao.UserDao
import com.receiptshares.user.exceptions.EmailNotUniqueException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Component
@RestController
@RequestMapping("/reg")
class RegistrationController {

    UserDao userDao

    @Autowired
    RegistrationController(UserDao userDao) {
        this.userDao = userDao
    }

    @RequestMapping(method = RequestMethod.POST)
    def registerNewUser(NewUserDTO newUserDTO) {
        userDao.registerNewUser(newUserDTO)
    }

    @ExceptionHandler
    def notUniqueEmail(EmailNotUniqueException exc) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body({email: exc.email})
    }
}