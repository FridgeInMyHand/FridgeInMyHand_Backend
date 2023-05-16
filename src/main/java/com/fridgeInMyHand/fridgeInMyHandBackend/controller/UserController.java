package com.fridgeInMyHand.fridgeInMyHandBackend.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgeInMyHand.fridgeInMyHandBackend.model.FridgeUser;
import com.fridgeInMyHand.fridgeInMyHandBackend.model.QFridgeUser;
import com.fridgeInMyHand.fridgeInMyHandBackend.repository.FridgeUserRepository;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

@CrossOrigin
@RestController
@Component
@PersistenceContext
public class UserController {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private FridgeUserRepository fridgeUserRepository;

    static class GetUserRequest {
        public String UUID;
    }

    static class GetUserResponse {

        public Double lat;
        @JsonProperty("long")
        public Double Long;
        public String url;
    }

    static class UserLocationRequest {
        public String UUID;
        public Double lat;
        @JsonProperty("long")
        public Double Long;
    }

    static class UserURLRequest {

        public String UUID;
        public String url;
    }

    static class NearByUserRequest {
        public String UUID;
        public Double lat;
        @JsonProperty("long")
        public Double Long;
        public Double lat_limit;
        public Double long_limit;
    }

    static class NearByUserResponse implements  Comparable<NearByUserResponse> {
        public NearByUserResponse(String _uuid, Double _lat, Double _long) {
            UUID = _uuid;
            lat = _lat;
            Long = _long;
        }

        public String UUID;
        public Double lat;
        @JsonProperty("long")
        public Double Long;

        @Override
        public int compareTo(NearByUserResponse nearByUserResponse) {
            return UUID.compareTo(nearByUserResponse.UUID);
        }
    }

    static class CommonError {
        public CommonError(String _error) {
            error = _error;
        }

        public String error;
    }

    @GetMapping("/user")
    @Transactional
    public ResponseEntity<String> getUser(@RequestBody String jsonBody) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        try {
            GetUserRequest req = mapper.readValue(jsonBody, GetUserRequest.class);
            var q = QFridgeUser.fridgeUser;
            var result = jpaQueryFactory.from(q).where(q.userUUID.eq(req.UUID)).select(q.latitude, q.longitude, q.userURL).fetchFirst();

            GetUserResponse res = new GetUserResponse();
            res.lat = result.get(0, Double.class);
            res.Long = result.get(1, Double.class);
            res.url = result.get(2, String.class);

            return ResponseEntity.ok(mapper.writeValueAsString(res));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(mapper.writeValueAsString(new CommonError(e.getMessage())));
        }
    }

    @PostMapping("/userLocation")
    @Transactional
    public ResponseEntity<String> setUserLocation(@RequestBody String jsonBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            UserLocationRequest req = mapper.readValue(jsonBody, UserLocationRequest.class);
            var q = QFridgeUser.fridgeUser;
            var result = jpaQueryFactory.from(q).where(q.userUUID.eq(req.UUID)).select(q).fetch();

            if (result.stream().count() == 0) {
                var newUser = new FridgeUser();
                newUser.userUUID = req.UUID;
                newUser.latitude = req.lat;
                newUser.longitude = req.Long;

                fridgeUserRepository.save(newUser);
            } else if (result.stream().count() == 1) {
                var user = result.get(0);
                user.longitude = req.Long;
                user.latitude = req.lat;
            } else {
                throw new IllegalStateException("duplicated UUID (server error): " + req.UUID);
            }

            return ResponseEntity.ok("");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(mapper.writeValueAsString(new CommonError(e.getMessage())));
        }
    }

    @PostMapping("/userURL")
    @Transactional
    public ResponseEntity<String> setUserURL(@RequestBody String jsonBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserURLRequest req = mapper.readValue(jsonBody, UserURLRequest.class);
            var q = QFridgeUser.fridgeUser;
            var result = jpaQueryFactory.from(q).where(q.userUUID.eq(req.UUID)).select(q).fetch();

            if (result.stream().count() == 0) {
                var newUser = new FridgeUser();
                newUser.userUUID = req.UUID;
                newUser.userURL = req.url;

                fridgeUserRepository.save(newUser);
            } else if (result.stream().count() == 1) {
                var user = result.get(0);
                user.userURL = req.url;
            } else {
                throw new IllegalStateException("duplicated UUID (server error): " + req.UUID);
            }

            return ResponseEntity.ok("");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(mapper.writeValueAsString(new CommonError(e.getMessage())));
        }
    }

    @GetMapping("/nearbyUser")
    @Transactional
    public ResponseEntity<String> getNearByUser(@RequestBody String jsonBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            NearByUserRequest req = mapper.readValue(jsonBody, NearByUserRequest.class);
            var q = QFridgeUser.fridgeUser;
            var result = jpaQueryFactory.from(q).select(q).fetch();

            var filtered = result.stream().filter(x -> (Math.abs(x.latitude - req.lat) < req.lat_limit) && (Math.abs(x.longitude - req.Long) < req.long_limit)).map(x -> new NearByUserResponse(x.userUUID, x.latitude, x.longitude)).sorted().toList();

            return ResponseEntity.ok(mapper.writeValueAsString(filtered));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(mapper.writeValueAsString(new CommonError(e.getMessage())));
        }
    }
}
