package com.chenshinan.spock.api.controller;

import com.chenshinan.spock.api.dto.IssueTypeDTO;
import com.chenshinan.spock.api.service.IssueTypeService;
import com.chenshinan.spock.infra.exception.CsnException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author shinan.chen
 * @date 2018/9/24
 */
@RestController
@RequestMapping(value = "issue_type/{organization_id}")
public class SpockController {

    @Autowired
    private IssueTypeService issueTypeService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<IssueTypeDTO> queryById(@PathVariable("organization_id") Long organizationId, @PathVariable("id") Long issueTypeId) {
        return new ResponseEntity<>(issueTypeService.queryById(organizationId, issueTypeId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IssueTypeDTO> create(@PathVariable("organization_id") Long organizationId, @RequestBody @Valid IssueTypeDTO issueTypeDTO) {
        return new ResponseEntity<>(issueTypeService.create(organizationId, issueTypeDTO), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<IssueTypeDTO> update(@PathVariable("organization_id") Long organizationId, @PathVariable("id") Long issueTypeId,
                                               @RequestBody @Valid IssueTypeDTO issueTypeDTO) {
        issueTypeDTO.setId(issueTypeId);
        issueTypeDTO.setOrganizationId(organizationId);
        return new ResponseEntity<>(issueTypeService.update(issueTypeDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("organization_id") Long organizationId, @PathVariable("id") Long issueTypeId) {
        return new ResponseEntity<>(issueTypeService.delete(organizationId, issueTypeId), HttpStatus.OK);
    }

    @GetMapping(value = "/check_delete/{id}")
    public ResponseEntity<Map<String, Object>> checkDelete(@PathVariable("organization_id") Long organizationId, @PathVariable("id") Long issueTypeId) {
        return new ResponseEntity<>(issueTypeService.checkDelete(organizationId, issueTypeId), HttpStatus.OK);
    }

    @GetMapping(value = "/check_name")
    public ResponseEntity<Boolean> checkName(@PathVariable("organization_id") Long organizationId,
                                             @RequestParam("name") String name,
                                             @RequestParam(value = "id", required = false) Long id) {
        return new ResponseEntity<>(issueTypeService.checkName(organizationId, name, id), HttpStatus.OK);
    }

    @GetMapping(value = "/types")
    public ResponseEntity<List<IssueTypeDTO>> queryByOrgId(@PathVariable("organization_id") Long organizationId) {
        return Optional.ofNullable(issueTypeService.queryByOrgId(organizationId))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CsnException("error.issue.queryByOrgId"));
    }
}
