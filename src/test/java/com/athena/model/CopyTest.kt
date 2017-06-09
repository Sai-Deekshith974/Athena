package com.athena.model

import com.athena.repository.BookRepository
import com.athena.repository.CopyRepository
import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import javax.transaction.Transactional

/**
 * Created by Tommy on 2017/6/9.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
@TestExecutionListeners(DependencyInjectionTestExecutionListener::class, DbUnitTestExecutionListener::class, TransactionalTestExecutionListener::class)
@DatabaseSetup("classpath:books.xml", "classpath:publishers.xml", "classpath:copies.xml")
open class CopyTest {
    @Autowired var copyRepository: CopyRepository? = null
    @Autowired var bookRepository: BookRepository? = null

    @Test fun testCopy() {
        val result = copyRepository!!.findOne(CopyPK(bookRepository!!.findOne(9787111124444L), 1))
        val except = "C程序设计"
        Assert.assertEquals(except, result.book.title)
    }

    @Test fun testBookCopy() {
        val book = bookRepository!!.findOne(9787111124444L)
        val result = book.copies[0]
        val except = copyRepository!!.findOne(CopyPK(bookRepository!!.findOne(9787111124444L), 1))
        Assert.assertEquals(except, result)
    }
}
