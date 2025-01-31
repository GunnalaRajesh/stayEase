package com.application.StayEase.controller;

import com.application.StayEase.dto.RoomDto;
import com.application.StayEase.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/hotels/{hotelId}/rooms")
@Slf4j
public class RoomAdminController {

    private final RoomService roomService;


    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId,
                                                 @RequestBody RoomDto roomDto){
        RoomDto room = roomService.createNewRoom(hotelId, roomDto);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @GetMapping(path = "/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId){
              return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping(path = "/{roomId}")
    public ResponseEntity<RoomDto> deleteRoomById(@PathVariable Long roomId){
            roomService.deleteRoomById(roomId);
            return ResponseEntity.noContent().build();
    }

}
