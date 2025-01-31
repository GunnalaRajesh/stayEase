package com.application.StayEase.service;

import com.application.StayEase.dto.RoomDto;
import com.application.StayEase.entity.Hotel;
import com.application.StayEase.entity.Room;
import com.application.StayEase.exception.ResourceNotFoundException;
import com.application.StayEase.repository.HotelRepository;
import com.application.StayEase.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomServiceImplementation implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("creating a new room in hotel with Id: {}" + hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("hotel not found with id: " + hotelId));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        if(hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);
        }
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("getting room with Id: {}" + roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("room not found with id: " + roomId));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("getting all the rooms in a hotel with Id: {}" + hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("hotel not found with id: " + hotelId));

        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRoomById(Long roomId) {
        log.info("deleting room with id: {}" + roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("room not found with id: " + roomId));
        roomRepository.deleteById(roomId);
        inventoryService.deleteFutureInventories(room);


    }
}
