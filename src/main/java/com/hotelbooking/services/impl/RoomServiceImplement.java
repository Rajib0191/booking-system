package com.hotelbooking.services.impl;

import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.dtos.RoomDto;
import com.hotelbooking.entities.Room;
import com.hotelbooking.enums.RoomType;
import com.hotelbooking.exceptions.InvalidBookingStateAndDateException;
import com.hotelbooking.exceptions.NotFoundException;
import com.hotelbooking.repositories.RoomRepository;
import com.hotelbooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImplement implements RoomService {
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + File.separator + "product-image";

    @Override
    public ResponseDto addRoom(RoomDto roomDto, MultipartFile imageFile) {
        Room roomToSave = modelMapper.map(roomDto,Room.class);

        if(imageFile != null){
            String imagePath = saveImage(imageFile);
            roomToSave.setImageUrl(imagePath);
        }

        roomRepository.save(roomToSave);
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Room Successfully Added!")
                .build();
    }

    @Override
    public ResponseDto updateRoom(RoomDto roomDto, MultipartFile imageFile) {
        Room existingRoom = roomRepository.findById(roomDto.getId()).
                orElseThrow(()-> new NotFoundException("Room not found!"));

        if(imageFile != null){
            String imagePath = saveImage(imageFile);
            existingRoom.setImageUrl(imagePath);
        }
        if(roomDto.getRoomNumber() != null && roomDto.getRoomNumber() >=0){
            existingRoom.setRoomNumber(roomDto.getRoomNumber());
        }
        if(roomDto.getPricePerNight() != null && roomDto.getPricePerNight().compareTo(BigDecimal.ZERO) >=0){
            existingRoom.setPricePerNight(roomDto.getPricePerNight());
        }
        if(roomDto.getCapacity() != null && roomDto.getCapacity() >= 0){
            existingRoom.setCapacity(roomDto.getCapacity());
        }
        if(roomDto.getCapacity() != null && roomDto.getCapacity() >= 0){
            existingRoom.setCapacity(roomDto.getCapacity());
        }
        if(roomDto.getType() != null) existingRoom.setRoomType(roomDto.getType());
        if (roomDto.getDescription() != null) existingRoom.setDescription(roomDto.getDescription());
try {
    roomRepository.save(existingRoom);
}catch (Exception e){
    e.printStackTrace();
}
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Room updated successfully")
                .build();
    }

    @Override
    public ResponseDto getAllRooms() {
        List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<RoomDto> roomDtoList = modelMapper.map(roomList, new TypeToken<List<RoomDto>>() {}.getType());
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .rooms(roomDtoList)
                .build();
    }

    @Override
    public ResponseDto getRoomById(Long id) {
        Room existingRoom = roomRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Room not found!"));

        RoomDto roomDto = modelMapper.map(existingRoom,RoomDto.class);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .room(roomDto)
                .build();
    }

    @Override
    public ResponseDto deleteRoom(Long id) {
        if(!roomRepository.existsById(id)){
            throw new NotFoundException("Room not found");
        }
        roomRepository.deleteById(id);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Room deleted successfully")
                .build();
    }

    @Override
    public ResponseDto getAvailableRoomRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
         //Validate Check-in date is not before today
        if(checkInDate.isBefore(LocalDate.now())){
            throw new InvalidBookingStateAndDateException("check in date cannot be before today");
        }

        //Validate Check-out date is not before check-in date
        if(checkOutDate.isBefore(checkInDate)){
            throw new InvalidBookingStateAndDateException("check out date cannot be before check in date");
        }

        //Validate Check-in date is not same check-out date
        if(checkInDate.isEqual(checkOutDate)){
            throw new InvalidBookingStateAndDateException("check in date cannot be equal check out date");
        }

        List<Room> roomList = roomRepository.findAvailableRooms(checkInDate, checkOutDate,roomType);
        List<RoomDto> roomDtoList = modelMapper.map(roomList, new TypeToken<List<RoomDto>>() {}.getType());

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .rooms(roomDtoList)
                .build();
    }

    @Override
    public ResponseDto searchRoom(String input) {
        List<Room> roomList = roomRepository.searchRooms(input);
        List<RoomDto> roomDtoList = modelMapper.map(roomList, new TypeToken<List<RoomDto>>() {}.getType());

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .rooms(roomDtoList)
                .build();
    }

    // Save Image Into IMAGE_DIRECTORY
    private String saveImage(MultipartFile imageFile){
        if(!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }

        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        try {
            File destinationFile = new File(directory, uniqueFileName);
            imageFile.transferTo(destinationFile);
            return "product-image/" + uniqueFileName;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to save image: " + ex.getMessage());
        }
    }
}
