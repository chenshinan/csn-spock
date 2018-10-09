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
        if (!checkName(organizationId, issueTypeDTO.getName(), null)) {
            throw new CsnException("error.issueType.checkName");
        }
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
        if (issueTypeDTO.getName() != null && !checkName(issueTypeDTO.getOrganizationId(), issueTypeDTO.getName(), issueTypeDTO.getId())) {
            throw new CsnException("error.issueType.checkName");
        }
        IssueType issueType = modelMapper.map(issueTypeDTO, IssueType.class);
        int isUpdate = issueTypeMapper.updateByPrimaryKeySelective(issueType);
        if (isUpdate != 1) {
            throw new CsnException("error.issueType.update");
        }
        issueType = issueTypeMapper.selectByPrimaryKey(issueType.getId());
        return modelMapper.map(issueType, IssueTypeDTO.class);
    }

    @Override
    public Map<String, Object> checkDelete(Long organizationId, Long issueTypeId) {
        Map<String, Object> result = new HashMap<>();
        result.put("canDelete", true);
        IssueType issueType = issueTypeMapper.selectByPrimaryKey(issueTypeId);
        if (issueType == null) {
            throw new CsnException("error.base.notFound");
        } else if (!issueType.getOrganizationId().equals(organizationId)) {
            throw new CsnException("error.issueType.illegal");
        }
        return result;
    }

    @Override
    public Boolean delete(Long organizationId, Long issueTypeId) {
        Map<String, Object> result = checkDelete(organizationId, issueTypeId);
        Boolean canDelete = (Boolean) result.get("canDelete");
        if (canDelete) {
            int isDelete = issueTypeMapper.deleteByPrimaryKey(issueTypeId);
            if (isDelete != 1) {
                throw new CsnException("error.state.delete");
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkName(Long organizationId, String name, Long id) {
        IssueType select = new IssueType();
        select.setName(name);
        select.setOrganizationId(organizationId);
        select = issueTypeMapper.selectOne(select);
        if (select != null) {
            //若传了id，则为更新校验（更新校验不校验本身），不传为创建校验返回false
            return select.getId().equals(id);
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
