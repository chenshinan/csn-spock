package com.chenshinan.spock.api.service.impl;

import com.chenshinan.spock.api.dto.IssueTypeDTO;
import com.chenshinan.spock.api.service.IssueTypeService;
import com.chenshinan.spock.domain.IssueType;
import com.chenshinan.spock.infra.exception.CsnException;
import com.chenshinan.spock.infra.mapper.IssueTypeMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shinan.chen
 * @Date 2018/8/8
 */
@Component
public class IssueTypeServiceImpl implements IssueTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueTypeServiceImpl.class);

    @Autowired
    private IssueTypeMapper issueTypeMapper;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public IssueTypeDTO queryById(Long organizationId, Long issueTypeId) {
        IssueType issueType = issueTypeMapper.selectByPrimaryKey(issueTypeId);
        if (issueType != null) {
            return modelMapper.map(issueType, IssueTypeDTO.class);
        }
        return null;
    }

    @Override
    public IssueTypeDTO create(Long organizationId, IssueTypeDTO issueTypeDTO) {
        issueTypeDTO.setOrganizationId(organizationId);
        IssueType issueType = modelMapper.map(issueTypeDTO, IssueType.class);
        if (issueTypeMapper.insert(issueType) != 1) {
            throw new CsnException("error.issueType.create");
        }
        issueType = issueTypeMapper.selectByPrimaryKey(issueType.getId());
        return modelMapper.map(issueType, IssueTypeDTO.class);
    }

    @Override
    public IssueTypeDTO update(IssueTypeDTO issueTypeDTO) {
        IssueType issueType = modelMapper.map(issueTypeDTO, IssueType.class);
        int isUpdate = issueTypeMapper.updateByPrimaryKeySelective(issueType);
        if (isUpdate != 1) {
            throw new CsnException("error.issueType.update");
        }
        issueType = issueTypeMapper.selectByPrimaryKey(issueType.getId());
        return modelMapper.map(issueType, IssueTypeDTO.class);
    }

    @Override
    public Boolean delete(Long organizationId, Long issueTypeId) {
        int isDelete = issueTypeMapper.deleteByPrimaryKey(issueTypeId);
        if (isDelete != 1) {
            throw new CsnException("error.state.delete");
        }
        return true;
    }

    @Override
    public List<IssueTypeDTO> queryByOrgId(Long organizationId) {
        IssueType issueType = new IssueType();
        issueType.setOrganizationId(organizationId);
        List<IssueType> issueTypes = issueTypeMapper.select(issueType);
        return modelMapper.map(issueTypes, new TypeToken<List<IssueTypeDTO>>() {
        }.getType());
    }
}
