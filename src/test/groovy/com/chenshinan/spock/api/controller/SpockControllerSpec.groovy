package com.chenshinan.spock.api.controller

import com.chenshinan.spock.api.dto.IssueTypeDTO
import com.chenshinan.spock.config.IntegrationTestConfiguration
import com.chenshinan.spock.domain.IssueType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
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

    /**
     * 测试编写说明：
     * 1、setup初始化数据执行在每一个单元测试的每一条测试数据之前
     * 2、调用controller方法失败（格式错误等），entity为空，actRequest为false
     * 3、调用成功，执行controller方法时抛出异常，未处理的异常，entity返回值非200，actRequest为false
     * 4、调用成功，执行controller方法时抛出异常，被捕获，entity返回值为200，actRequest为true
     * 5、在path和param中的参数均有null校验，若传null，调用成功，被spring抛出异常，被捕获，entity返回值为200，actRequest为true
     */

    //初始化5条数据
    def setup() {
        println "执行初始化"
        for (int i = 1; i <= 5; i++) {
            IssueTypeDTO issueTypeDTO = new IssueTypeDTO()
            issueTypeDTO.setId(i)
            issueTypeDTO.setName("init_name" + i)
            issueTypeDTO.setIcon("init_icon" + i)
            issueTypeDTO.setDescription("init_description" + i)
            issueTypeDTO.setOrganizationId(testOrginzationId)
            issueTypeDTO = issueTypeService.create(testOrginzationId, issueTypeDTO)
            list.add(issueTypeDTO)
        }
    }

    def cleanup() {
        IssueType del = new IssueType()
        issueTypeService.delete(del);//清空数据
        list.clear();
    }

    def "create"() {
        given: '准备工作'
        IssueTypeDTO issueTypeDTO = new IssueTypeDTO();
        issueTypeDTO.setName(name);
        issueTypeDTO.setIcon(icon);
        issueTypeDTO.setDescription(description);
        issueTypeDTO.setOrganizationId(testOrginzationId);

        when: '创建问题类型'
        HttpEntity<IssueTypeDTO> httpEntity = new HttpEntity<>(issueTypeDTO)
        def entity = restTemplate.exchange(baseUrl, HttpMethod.POST, httpEntity, IssueTypeDTO, testOrginzationId)

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    if (entity.getBody().getId() != null) {
                        actResponse = true
                    }
                }
            }
        }
        actRequest == expRequest
        actResponse == expResponse

        where: '测试用例：'
        name         | icon    | description    || expRequest | expResponse
        'name1'      | 'icon1' | 'description1' || true       | true
        null         | 'icon1' | 'description1' || true       | false
        'name1'      | null    | 'description1' || true       | true
        'name1'      | 'icon1' | null           || true       | true
        'init_name1' | 'icon1' | 'description1' || true       | false
    }

    def "update"() {
        given: '准备工作'
        IssueTypeDTO issueTypeDTO = list.get(0);
        issueTypeDTO.setName(name);
        issueTypeDTO.setIcon(icon);
        issueTypeDTO.setDescription(description);
        issueTypeDTO.setOrganizationId(testOrginzationId);

        when: '更新问题类型'
        HttpEntity<IssueTypeDTO> httpEntity = new HttpEntity<>(issueTypeDTO)
        def entity = restTemplate.exchange(baseUrl + '/{id}', HttpMethod.PUT, httpEntity, IssueTypeDTO, testOrginzationId, issueTypeDTO.getId())

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    if (entity.getBody().getId() != null) {
                        actResponse = true
                    }
                }
            }
        }
        actRequest == expRequest
        actResponse == expResponse

        where: '测试用例：'
        name         | icon    | description    || expRequest | expResponse
        'name1'      | 'icon1' | 'description1' || true       | true
        null         | 'icon1' | 'description1' || true       | false
        'name1'      | null    | 'description1' || true       | true
        'name1'      | 'icon1' | null           || true       | true
        'init_name2' | 'icon1' | 'description1' || true       | false
    }

    def "checkDelete"() {
        given: '准备工作'
        def issueTypeId = id

        when: '校验问题类型是否可以删除'
        def entity = restTemplate.exchange(baseUrl + "/check_delete/{id}", HttpMethod.GET, null, Map, testOrginzationId, issueTypeId)

        then: '状态码为200，调用成功'

        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    if (entity.getBody().get('canDelete') != null) {
                        actResponse = entity.getBody().get('canDelete')
                    }
                }
            }
        }
        actRequest == expRequest
        actResponse == expResponse

        where: '测试用例：'
        id     || expRequest | expResponse
        '1'    || true       | true
        '9999' || true       | false
        null   || false      | false
    }

    def "delete"() {
        given: '准备工作'
        def issueTypeId = id

        when: '删除问题类型'
        def entity = restTemplate.exchange(baseUrl + "/{id}", HttpMethod.DELETE, null, Object, testOrginzationId, issueTypeId)

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null && entity.getBody() instanceof Boolean) {
                    actResponse = entity.getBody()
                }
            }
        }
        actRequest == expRequest
        actResponse == expResponse

        where: '测试用例：'
        id     || expRequest | expResponse
        '1'    || true       | true
        '9999' || true       | false
        null   || false      | false
    }

    def "checkName"() {
        given: '准备工作'
        def url = baseUrl + "/check_name?1=1"
        if (name != null) {
            url = url + "&name=" + name
        }
        if (id != null) {
            url = url + "&id=" + id
        }

        when: '校验问题类型名字是否未被使用'
        def entity = restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class, testOrginzationId)

        then: '状态码为200，调用成功'

        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                actResponse = entity.getBody()
            }
        }
        actRequest == expRequest
        actResponse == expResponse
//        then:
//        thrown(Exception)

        where: '测试用例：'
        name         | id   || expRequest | expResponse
        'init_name1' | null || true       | false
        'init_name1' | '1'  || true       | true
        'name1'      | null || true       | true
    }

    def "queryByOrgId"() {
        when: '获取问题类型列表'
        ParameterizedTypeReference<List<IssueTypeDTO>> typeRef = new ParameterizedTypeReference<List<IssueTypeDTO>>() {
        };
        def entity = restTemplate.exchange(baseUrl + "/types", HttpMethod.GET, null, typeRef, testOrginzationId)

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponseSize = 0;
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    actResponseSize = entity.getBody().size()
                }

            }
        }
        actRequest == expRequest
        actResponseSize == expResponseSize

        where: '测试用例：'
        expRequest | expResponseSize
        true       | 5
    }

    def "queryById"() {
        given: '准备工作'
        def issueTypeId = id

        when: '根据id查询问题类型'
        def entity = restTemplate.exchange(baseUrl + "/{id}", HttpMethod.GET, null, IssueTypeDTO, testOrginzationId, issueTypeId)

        then: '状态码为200，调用成功'

        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    if (entity.getBody().getId() != null) {
                        actResponse = true
                    }
                }
            }
        }
        actRequest == expRequest
        actResponse == expResponse

        where: '测试用例：'
        id     || expRequest | expResponse
        '1'    || true       | true
        '9999' || true       | false
        null   || true       | false
    }
}
