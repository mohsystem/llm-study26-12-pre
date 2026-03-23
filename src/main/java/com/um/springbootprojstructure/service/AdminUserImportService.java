package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.XmlImportSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AdminUserImportService {
    XmlImportSummaryResponse importLegacyUsersXml(MultipartFile file);
}
