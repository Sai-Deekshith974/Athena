package com.athena.repository

import com.athena.repository.mongo.CopyDamageReportRepository
import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.lordofthejars.nosqlunit.annotation.UsingDataSet
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule
import com.mongodb.gridfs.GridFSDBFile
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.core.io.Resource
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.util.DigestUtils
import org.springframework.util.MimeType
import java.net.URLConnection

/**
 * Created by Tommy on 2017/12/5.
 *
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener::class, DbUnitTestExecutionListener::class, TransactionalTestExecutionListener::class)
class CopyDamageReportRepositoryTest {

    @Autowired
    private val applicationContext: ApplicationContext? = null

    @Autowired
    lateinit var copyDamageReportRepository: CopyDamageReportRepository

    @Autowired
    lateinit var gridFsTemplate: GridFsTemplate

    @get:Rule public var embeddedMongoRule: MongoDbRule = newMongoDbRule().defaultSpringMongoDb("Athena")

    lateinit var uploadedImageResource: Resource

    @Before
    fun setup() {
    }

    private fun loadImage() {
        //load image file
        this.uploadedImageResource = this.applicationContext!!.getResource("classpath:image/20141214_152457000_iOS.jpg")
    }

    private fun cleanUpImage() {
        this.gridFsTemplate.delete(Query(Criteria.where("_id").exists(true)))
    }

    @Test
    @UsingDataSet(locations = arrayOf("/copy_damage_report.json"), loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    fun testGetCopyDamageReport_ShouldGetOne() {
        val copyDamageReport = this.copyDamageReportRepository.findOne("e6062fd6-e840-420d-8113-e7b37382a4a4")
        Assert.assertNotNull(copyDamageReport)
        Assert.assertEquals("Damage is not so bad", copyDamageReport.description)
    }

    @Test
    @UsingDataSet(locations = arrayOf("/copy_damage_report.json"), loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    fun testSetImage_ShouldCanSaveAndLoad() {
        this.loadImage()
        val mimeType = URLConnection.guessContentTypeFromName(this.uploadedImageResource.filename)
        var copyDamageReport = this.copyDamageReportRepository.findOne("e6062fd6-e840-420d-8113-e7b37382a4a4")
        copyDamageReport = this.copyDamageReportRepository.setImage(copyDamageReport, this.uploadedImageResource.inputStream, MimeType.valueOf(mimeType))
        try {
            Assert.assertNotNull(copyDamageReport.imageId)

            val retrievedImages: MutableList<GridFSDBFile>? = this.gridFsTemplate.find(Query(Criteria.where("_id").`is`(copyDamageReport.imageId)))

            Assert.assertEquals(1, retrievedImages!!.count())

            val retrievedImage = retrievedImages[0]

            Assert.assertEquals(DigestUtils.md5DigestAsHex(this.uploadedImageResource.inputStream), retrievedImage.mD5)

        } finally {
            this.cleanUpImage()
        }
    }


}