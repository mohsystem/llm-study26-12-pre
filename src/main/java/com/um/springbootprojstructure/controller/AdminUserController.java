package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.AdminUserMergeRequest;
import com.um.springbootprojstructure.dto.AdminUserMergeResultResponse;
import com.um.springbootprojstructure.dto.DirectoryMatchResponse;
import com.um.springbootprojstructure.dto.DirectoryValidationRequest;
import com.um.springbootprojstructure.dto.UpdateUserRoleRequest;
import com.um.springbootprojstructure.dto.UpdateUserRoleResponse;
import com.um.springbootprojstructure.dto.XmlImportSummaryResponse;
import com.um.springbootprojstructure.service.AdminUserImportService;
import com.um.springbootprojstructure.service.AdminUserService;
import com.um.springbootprojstructure.service.DirectoryValidationService;
import com.um.springbootprojstructure.service.PromptLogService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AdminUserImportService adminUserImportService;
    private final DirectoryValidationService directoryValidationService;
    private final PromptLogService promptLogService;

    public AdminUserController(AdminUserService adminUserService,
                               AdminUserImportService adminUserImportService,
                               DirectoryValidationService directoryValidationService,
                               PromptLogService promptLogService) {
        this.adminUserService = adminUserService;
        this.adminUserImportService = adminUserImportService;
        this.directoryValidationService = directoryValidationService;
        this.promptLogService = promptLogService;
    }

    @PostMapping("/merge")
    public AdminUserMergeResultResponse merge(@Valid @RequestBody AdminUserMergeRequest request) {
        promptLogService.logPrompt("HTTP POST /api/admin/users/merge source=" +
                request.getSourcePublicRef() + " target=" + request.getTargetPublicRef());
        return adminUserService.mergeUsers(request.getSourcePublicRef(), request.getTargetPublicRef());
    }

    @PutMapping("/{id}/role")
    public UpdateUserRoleResponse updateRole(@PathVariable Long id,
                                             @Valid @RequestBody UpdateUserRoleRequest request) {
        promptLogService.logPrompt("HTTP PUT /api/admin/users/" + id + "/role role=" + request.getRole());
        return adminUserService.updateUserRole(id, request.getRole());
    }

    @PostMapping(value = "/import-xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public XmlImportSummaryResponse importXml(@RequestPart("file") MultipartFile file) {
        promptLogService.logPrompt("HTTP POST /api/admin/users/import-xml file=" + file.getOriginalFilename());
        return adminUserImportService.importLegacyUsersXml(file);
    }

    @PostMapping("/validate-directory")
    public DirectoryMatchResponse validateDirectory(@Valid @RequestBody DirectoryValidationRequest request) {
        promptLogService.logPrompt("HTTP POST /api/admin/users/validate-directory email=" + request.getEmail());
        return directoryValidationService.validate(request);
    }
}
