package com.athena.controller;

import com.athena.model.User;
import com.athena.security.model.Account;
import com.athena.security.model.JwtAuthenticationToken;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder

@RunWith(SpringRunner::class)
@SpringBootTest
@TestExecutionListeners(TransactionalTestExecutionListener::class, DbUnitTestExecutionListener::class, DependencyInjectionTestExecutionListener::class)
@DatabaseSetup("classpath:books.xml", "classpath:publishers.xml", "classpath:users.xml")
@WebAppConfiguration
open class BookControllerTest {
    @Autowired private val context: WebApplicationContext? = null

    private var mvc: MockMvc? = null

    @Before fun setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context!!).apply<DefaultMockMvcBuilder>(springSecurity()).build()

    }

    @Test
    fun testBookSearch() {
        val securityContext = SecurityContextHolder.createEmptyContext()
        val encoder = BCryptPasswordEncoder()

        val user = User()
        user.identity = "READER"
        user.username = "reader"
        user.password = encoder.encode("123456")
        user.id = 1L
        user.email = "test@test.com"
        user.wechatId = "testWechat"
        user.phoneNumber = "11111111111"
        val principal = Account(user)

        val authentication = JwtAuthenticationToken(principal, true)

        securityContext.authentication = authentication
        mvc!!.perform(get("/api/v1/books?title=elit").with(securityContext(securityContext))).andExpect(content().json("{\"content\":[{\"isbn\":9784099507505,\"author\":[\"Steffen Catcherside\"],\"translator\":[],\"publishDate\":\"2016-09-18\",\"categoryId\":\"TC331A\",\"version\":4,\"coverUrl\":null,\"preface\":null,\"introduction\":null,\"directory\":null,\"title\":\"adipiscing elit\",\"titlePinyin\":null,\"titleShortPinyin\":null,\"subtitle\":null,\"language\":\"English\",\"price\":520.5}],\"totalElements\":1,\"last\":true,\"totalPages\":1,\"number\":0,\"size\":20,\"sort\":null,\"first\":true,\"numberOfElements\":1}\n"))
    }
}
