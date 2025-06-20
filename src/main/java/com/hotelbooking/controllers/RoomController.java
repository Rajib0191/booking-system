package com.hotelbooking.controllers;

import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.dtos.RoomDto;
import com.hotelbooking.enums.RoomType;
import com.hotelbooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> addRoom(
            @RequestParam Integer roomNumber,
            @RequestParam RoomType type,
            @RequestParam BigDecimal pricePerNight,
            @RequestParam Integer capacity,
            @RequestParam String description,
            @RequestParam MultipartFile imageUrl
    ){
        RoomDto roomDto = RoomDto.builder()
                .roomNumber(roomNumber)
                .type(type)
                .pricePerNight(pricePerNight)
                .capacity(capacity)
                .description(description)
                .build();
        return ResponseEntity.ok(roomService.addRoom(roomDto,imageUrl));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> updateRoom(
            @RequestParam(value = "roomNumber",required = false) Integer roomNumber,
            @RequestParam(value = "type",required = false) RoomType type,
            @RequestParam(value = "pricePerNight",required = false) BigDecimal pricePerNight,
            @RequestParam(value = "capacity",required = false) Integer capacity,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "imageUrl",required = false) MultipartFile imageUrl,
            @RequestParam(value = "id",required = true) Long id
    ){
        RoomDto roomDto = RoomDto.builder()
                .id(id)
                .roomNumber(roomNumber)
                .type(type)
                .pricePerNight(pricePerNight)
                .capacity(capacity)
                .description(description)
                .build();
        return ResponseEntity.ok(roomService.updateRoom(roomDto,imageUrl));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDto> getAllRooms(){
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getRoomById(@PathVariable Long id){
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> deleteRoom(@PathVariable Long id){
        return ResponseEntity.ok(roomService.deleteRoom(id));
    }

    @GetMapping("/available")
    public ResponseEntity<ResponseDto> getAvailableRoom(
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate,
            @RequestParam(required = false) RoomType roomType
            ){
        return ResponseEntity.ok(roomService.getAvailableRoomRooms(checkInDate,checkOutDate,roomType));
    }

    @GetMapping("/types")
    public List<RoomType> getAllRoomTypes(){
        return Arrays.asList(RoomType.values());
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDto> searchRoom(@RequestParam String input){
        return ResponseEntity.ok(roomService.searchRoom(input));
    }

}
