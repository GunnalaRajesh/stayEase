package com.application.StayEase.service;

import com.application.StayEase.dto.HotelDto;
import com.application.StayEase.entity.Hotel;
import com.application.StayEase.entity.Room;
import com.application.StayEase.exception.ResourceNotFoundException;
import com.application.StayEase.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImplementation implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("creating a new hotel with name: {}", hotelDto.getName());
            Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
            hotel.setActive(false);
            hotel = hotelRepository.save(hotel);
            log.info("created hotel with name: {}", hotelDto.getName());
            return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("getting the hotel with id: {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hotel not found with id: " + id));
             return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating the hotel with id: {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hotel not found with id: " + id));
        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Transactional
    @Override
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hotel not found with id: " + id));
        boolean exists = hotelRepository.existsById(id);
        if(!exists) throw new ResourceNotFoundException("Hotel not found with id:" + id);

        hotelRepository.deleteById(id);

        for(Room room: hotel.getRooms()){
            inventoryService.deleteFutureInventories(room);
        }

    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating the hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("hotel not found with id: " + hotelId));
        hotel.setActive(true);

        //Assuming doing it once

        for(Room room: hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }

    }


}
