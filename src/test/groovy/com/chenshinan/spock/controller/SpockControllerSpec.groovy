package com.chenshinan.spock.controller

import com.chenshinan.spock.config.IntegrationTestConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration.class)
class SpockControllerSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @Shared
    Long orginzationId = 1L;

    @Shared
    String baseUrl = '/spock'

    //初始化数据
    def setup() {
        println "aa";
    }

    def cleanup() {
        println "bb";
    }

    def "queryById"() {
        when: '根据id查询页面'
        def entity = restTemplate.exchange(baseUrl + "/queryById/{id}", HttpMethod.GET, null, String, id)

        then: '结果判断'
        entity.getStatusCode().is2xxSuccessful() == isSuccess
        (entity.getBody() != null && entity.getBody() instanceof String) == reponseResult

        where: '测试用例：'
        id || isSuccess | reponseResult
//        null || true      | false
        1  || true      | true
    }
}
