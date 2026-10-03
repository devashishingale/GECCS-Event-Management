package com.geccs.eventmanagement.service;

import com.geccs.eventmanagement.dto.CouncilResponse;
import com.geccs.eventmanagement.entity.Council;
import com.geccs.eventmanagement.repository.CouncilRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouncilService {

    private final CouncilRepository councilRepository;

    public CouncilService(CouncilRepository councilRepository) {
        this.councilRepository = councilRepository;
    }

    public List<CouncilResponse> getAllCouncils() {

        return councilRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CouncilResponse getCouncilByCode(String code) {

        Council council = councilRepository.findByCode(code)
                .orElseThrow(() ->
                        new IllegalArgumentException("Council not found: " + code)
                );

        return convertToResponse(council);
    }

    private CouncilResponse convertToResponse(Council council) {

        CouncilResponse response = new CouncilResponse();

        response.setId(council.getId());
        response.setName(council.getName());
        response.setCode(council.getCode());
        response.setType(council.getType());
        response.setActive(council.isActive());

        if (council.getAcademicUnit() != null) {
            response.setAcademicUnitName(
                    council.getAcademicUnit().getName()
            );

            response.setAcademicUnitCode(
                    council.getAcademicUnit().getCode()
            );
        }

        return response;
    }
}