package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.DirectoryMatchResponse;
import com.um.springbootprojstructure.dto.DirectoryValidationRequest;

public interface DirectoryValidationService {
    DirectoryMatchResponse validate(DirectoryValidationRequest request);
}
