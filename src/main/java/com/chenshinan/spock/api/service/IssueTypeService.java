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

    List<IssueTypeDTO> queryByOrgId(Long organizationId);
}
