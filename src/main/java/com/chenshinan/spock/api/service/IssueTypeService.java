package com.chenshinan.spock.api.service;

import com.chenshinan.spock.api.dto.IssueTypeDTO;

import java.util.List;
import java.util.Map;

/**
 * @author shinan.chen
 * @Date 2018/8/8
 */
public interface IssueTypeService {

    IssueTypeDTO queryById(Long organizationId, Long issueTypeId);

    IssueTypeDTO create(Long organizationId, IssueTypeDTO issueTypeDTO);

    IssueTypeDTO update(IssueTypeDTO issueTypeDTO);

    Boolean delete(Long organizationId, Long issueTypeId);

    Map<String, Object> checkDelete(Long organizationId, Long issueTypeId);

    Boolean checkName(Long organizationId, String name, Long id);

    List<IssueTypeDTO> queryByOrgId(Long organizationId);
}
