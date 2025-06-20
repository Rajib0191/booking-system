package com.hotelbooking.services;

import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.dtos.RoomDto;
import com.hotelbooking.enums.RoomType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    ResponseDto addRoom(RoomDto roomDto, MultipartFile imageFile);
    ResponseDto updateRoom(RoomDto roomDto, MultipartFile imageFile);
    ResponseDto getAllRooms();
    ResponseDto getRoomById(Long id);
    ResponseDto deleteRoom(Long id);
    ResponseDto getAvailableRoomRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType);
    ResponseDto searchRoom(String input);
}
