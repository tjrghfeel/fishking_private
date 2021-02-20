package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.model.common.MainSpeciesResponse;

import java.util.List;

public interface CommonCodeRepositoryCustom {
    public List<MainSpeciesResponse> getMainSpeciesCount();
    public List<MainSpeciesResponse> getMainDistrictCount();
}
