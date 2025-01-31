package com.application.StayEase.service;

import java.util.List;

import com.application.StayEase.dto.RoomDto;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);

    RoomDto getRoomById(Long roomId);

    List<RoomDto> getAllRoomsInHotel(Long hotelId);

 //   RoomDto updateRoomById(Long roomId);

    void deleteRoomById(Long roomId);


}
