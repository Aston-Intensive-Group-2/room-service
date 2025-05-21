package aston.room_booking.room_service.service.implementation;

import aston.room_booking.room_service.dto.EquipmentDto;
import aston.room_booking.room_service.dto.RoomCreateRequestDto;
import aston.room_booking.room_service.dto.RoomDto;
import aston.room_booking.room_service.dto.RoomUpdateRequestDto;
import aston.room_booking.room_service.mapper.EquipmentMapper;
import aston.room_booking.room_service.mapper.RoomMapper;
import aston.room_booking.room_service.model.EquipmentType;
import aston.room_booking.room_service.model.RoomStatus;
import aston.room_booking.room_service.model.entity.EquipmentEntity;
import aston.room_booking.room_service.model.entity.RoomEntity;
import aston.room_booking.room_service.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomMapper roomMapper;
    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void getRoomList_ReturnsValidListOfRoomDto() {
        // given
        var expectedRoomEntity_1 = new RoomEntity(1L, 3, 4, 3, "expectedRoomAddress_1", RoomStatus.LOCKED, List.of(new EquipmentEntity(1L, "expectedEquipmentName_1", EquipmentType.SCANNER, null), new EquipmentEntity(2L, "expectedEquipmentName_2", EquipmentType.PRINTER, null)));
        var expectedRoomEntity_2 = new RoomEntity(2L, 5, 6, 3, "expectedRoomAddress_2", RoomStatus.UNLOCKED, List.of(new EquipmentEntity(3L, "expectedEquipmentName_3", EquipmentType.CONDITIONER, null), new EquipmentEntity(4L, "expectedEquipmentName_4", EquipmentType.PROJECTOR, null)));
        var expectedRoomDto_1 = new RoomDto(1L, 3, 4, 3, "expectedRoomAddress_1", RoomStatus.LOCKED, List.of(new EquipmentDto(1L, "expectedEquipmentName_1", EquipmentType.SCANNER, null), new EquipmentDto(2L, "expectedEquipmentName_2", EquipmentType.PRINTER, null)));
        var expectedRoomDto_2 = new RoomDto(2L, 5, 6, 3, "expectedRoomAddress_2", RoomStatus.UNLOCKED, List.of(new EquipmentDto(3L, "expectedEquipmentName_3", EquipmentType.CONDITIONER, null), new EquipmentDto(4L, "expectedEquipmentName_4", EquipmentType.PROJECTOR, null)));

        expectedRoomEntity_1.getEquipmentEntityList().get(0).setRoomEntity(expectedRoomEntity_1);
        expectedRoomEntity_1.getEquipmentEntityList().get(1).setRoomEntity(expectedRoomEntity_1);
        expectedRoomEntity_2.getEquipmentEntityList().get(0).setRoomEntity(expectedRoomEntity_2);
        expectedRoomEntity_2.getEquipmentEntityList().get(1).setRoomEntity(expectedRoomEntity_2);

        var expectedListOfRoomEntity = List.of(expectedRoomEntity_1, expectedRoomEntity_2);
        var expectedListOfRoomDto = List.of(expectedRoomDto_1, expectedRoomDto_2);

        when(this.roomRepository.findAll()).thenReturn(expectedListOfRoomEntity);
        when(this.roomMapper.toDto(expectedListOfRoomEntity)).thenReturn(expectedListOfRoomDto);

        // when
        var actualListOfRoomDto = this.roomService.getRoomList();

        // then
        assertNotNull(actualListOfRoomDto);
        assertEquals(expectedListOfRoomDto, actualListOfRoomDto);

        verify(this.roomRepository).findAll();
        verify(this.roomMapper).toDto(expectedListOfRoomEntity);
    }

    @Test
    void getFilteredRoomList() {
        // todo
    }

    @Test
    void getRoomById_IdIsValid_ReturnsValidRoomDto() {
        // given
        Long roomId = 1L;
        var expectedRoomEntity = new RoomEntity(1L, 3, 4, 3, "expectedRoomAddress_1", RoomStatus.LOCKED, List.of(new EquipmentEntity(1L, "expectedEquipmentName_1", EquipmentType.SCANNER, null), new EquipmentEntity(2L, "expectedEquipmentName_2", EquipmentType.PRINTER, null)));
        var expectedRoomDto = new RoomDto(1L, 3, 4, 3, "expectedRoomAddress_1", RoomStatus.LOCKED, List.of(new EquipmentDto(1L, "expectedEquipmentName_1", EquipmentType.SCANNER, null), new EquipmentDto(2L, "expectedEquipmentName_2", EquipmentType.PRINTER, null)));

        expectedRoomEntity.getEquipmentEntityList().get(0).setRoomEntity(expectedRoomEntity);
        expectedRoomEntity.getEquipmentEntityList().get(1).setRoomEntity(expectedRoomEntity);

        when(this.roomRepository.findById(roomId)).thenReturn(Optional.of(expectedRoomEntity));
        when(this.roomMapper.toDto(expectedRoomEntity)).thenReturn(expectedRoomDto);

        // when
        var actualRoomDto = this.roomService.getRoomById(roomId);

        // then
        assertNotNull(actualRoomDto);
        assertEquals(expectedRoomDto, actualRoomDto);

        verify(this.roomRepository).findById(roomId);
        verify(this.roomMapper).toDto(expectedRoomEntity);
    }

    @Test
    void createRoom_RoomCreateRequestDtoIsValid_ReturnsValidRoomDto() {
        // given
        var roomCreateRequestDto = new RoomCreateRequestDto(3, 3, 3, "Address");
        var expectedRoomEntity = new RoomEntity(1L, 3, 3, 3, "Address", RoomStatus.UNLOCKED, null);
        var expectedRoomDto = new RoomDto(1L, 3, 3, 3, "Address", RoomStatus.UNLOCKED, null);

        when(this.roomRepository.save(any(RoomEntity.class))).thenReturn(expectedRoomEntity);
        when(this.roomMapper.toDto(expectedRoomEntity)).thenReturn(expectedRoomDto);

        // when
        var actualRoomDto = this.roomService.createRoom(roomCreateRequestDto);

        // then
        assertNotNull(actualRoomDto);
        assertEquals(expectedRoomDto, actualRoomDto);

        verify(this.roomRepository).save(any(RoomEntity.class));
        verify(this.roomMapper).toDto(expectedRoomEntity);
    }

    @Test
    void updateRoomById_RoomUpdateRequestDtoAndIdIsValid_ReturnsValidRoomDto() {
        // given
        Long roomId = 1L;
        var roomUpdateRequestDto = new RoomUpdateRequestDto(3, 3, 3, "Address", RoomStatus.UNLOCKED, List.of());
        var currentRoomEntity = new RoomEntity(1L, 1, 1, 1, "Address", RoomStatus.UNLOCKED, List.of());
        var expectedRoomEntity = new RoomEntity(1L, 3, 3, 3, "Address", RoomStatus.UNLOCKED, List.of());
        var expectedRoomDto = new RoomDto(1L, 3, 3, 3, "Address", RoomStatus.UNLOCKED, List.of());

        when(this.roomRepository.findById(roomId)).thenReturn(Optional.of(currentRoomEntity));
        when(this.equipmentMapper.toEntity(anyList())).thenReturn(List.of());
        when(this.roomRepository.save(any(RoomEntity.class))).thenReturn(expectedRoomEntity);
        when(this.roomMapper.toDto(expectedRoomEntity)).thenReturn(expectedRoomDto);

        // when
        var actualRoomDto = this.roomService.updateRoomById(roomId, roomUpdateRequestDto);

        // then
        assertNotNull(actualRoomDto);
        assertEquals(expectedRoomDto, actualRoomDto);

        verify(this.roomRepository).findById(roomId);
        verify(this.equipmentMapper).toEntity(anyList());
        verify(this.roomRepository).save(any(RoomEntity.class));
        verify(this.roomMapper).toDto(expectedRoomEntity);
    }

    @Test
    void deleteRoomById_IdIsValid() {
        // given
        Long roomId = 1L;

        doNothing().when(this.roomRepository).deleteById(roomId);

        // when
        this.roomService.deleteRoomById(roomId);

        // then
        verify(this.roomRepository, times(1)).deleteById(roomId);
    }
}
