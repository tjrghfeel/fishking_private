package com.tobe.fishking.v2.service.smartfishing;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.entity.fishing.RealTimeVideo;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.model.response.FishingShipResponse;
import com.tobe.fishking.v2.repository.fishking.CompanyRepository;
import com.tobe.fishking.v2.repository.fishking.RealTimeVideoRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.service.HttpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FishingShipService {

    private final ShipRepository shipRepository;
    private final CompanyRepository companyRepository;
    private final HttpRequestService httpRequestService;
    private final RealTimeVideoRepository realTimeVideoRepository;

    @Transactional
    public Page<FishingShipResponse> getFishingShips(Long memberId, String keyword, String cameraActive, Integer page) throws EmptyListException {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("shipName").ascending());
        Page<FishingShipResponse> ships = shipRepository.getShipsByCompanyMember2(memberId, keyword, cameraActive, pageable);
        List<FishingShipResponse> contents = ships.getContent();
        if (contents.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return ships;
    }

    @Transactional
    public List<Map<String, Object>> getGoodsShips(Long memberId) throws EmptyListException {
        List<Map<String, Object>> response = new ArrayList<>();
        List<Tuple> tuples = shipRepository.getGoodsShips(memberId);
        for (Tuple t : tuples) {
            Map<String, Object> r = new HashMap<>();
            r.put("id", t.get(0, Long.class));
            r.put("shipName", t.get(1, String.class));
            response.add(r);
        }
        if (response.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return response;
    }

    @Transactional
    public List<Map<String, Object>> getCameraList(Member member) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, EmptyListException {
        Company company = companyRepository.findByMember(member);
        List<Map<String, Object>> response =  new ArrayList<>();
        List<RealTimeVideo> videos = realTimeVideoRepository.getRealTimeVideoByMember(member);
        String token = "";
        if (videos.size() > 0) {
            RealTimeVideo video = videos.get(0);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(Long.parseLong(video.getExpireTime())),
                    TimeZone.getDefault().toZoneId()
            );
            String expireTime = "";
            if (now.isAfter(expTime)) {
                Map<String, String> tokenData = httpRequestService.refreshToken(video.getToken());
                token = tokenData.get("token");
                expireTime = tokenData.get("expireTime");
                realTimeVideoRepository.updateToken(token, expireTime, video.getToken());
            } else {
                token = video.getToken();
                expireTime = video.getExpireTime();
            }
        } else {
            token = httpRequestService.getToken(company.getNhnId());
        }

        List<Map<String, Object>> cameras = httpRequestService.getCameraList(token);
        for (Map<String, Object> camera : cameras) {
            Map<String, Object> c = new HashMap<>();
            String serial = camera.get("serialNo").toString();
            String name = camera.get("labelName").toString();
            c.put("serial", serial);
            c.put("name", name);
            response.add(c);
        }
        if (response.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return response;
    }

}
