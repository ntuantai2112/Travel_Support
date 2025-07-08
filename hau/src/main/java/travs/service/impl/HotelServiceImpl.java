package travs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import travs.common.HotelType;
import travs.common.type.ApproveStatus;
import travs.common.type.ImageType;
import travs.common.type.RoomType;
import travs.exception.ErrorCode;
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.repository.AmenitiesRepository;
import travs.repository.FavoriteRepository;
import travs.repository.HotelBookItemRepository;
import travs.repository.HotelBookingReceiptRepository;
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.response.BaseResponse;
import travs.response.hotel.FacilityDTO;
import travs.response.hotel.HotelDTO;
import travs.response.hotel.HotelDetail;
import travs.response.hotel.HotelRoomDTO;
import travs.response.hotel.ImageDTO;
import travs.utils.AuthenticationUtils;
import travs.utils.FileStore;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;
import travs.entity.hotel.Amenities;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelBookableItem;
import travs.entity.hotel.HotelImage;
import travs.request.hotel.HotelRoomUploadBody;
import travs.request.hotel.HotelUploadBody;
import travs.service.HotelService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class HotelServiceImpl implements HotelService {
    private HotelRepository hotelRepository;
    private HotelBookItemRepository hotelBookItemRepository;
    private HotelImageRepository hotelImageRepository;
    private FavoriteRepository favoriteRepository;
    private AmenitiesRepository amenitiesRepository;

    @Override
    public BaseResponse get(String slug) {
        Hotel hotel = hotelRepository.findFirstBySlug(slug).orElseThrow(ResourceNotFoundException::new);
        List<Amenities> amenitiesList = amenitiesRepository.findAll();
        Map<Integer, Amenities> map = amenitiesList.stream().collect(Collectors.toMap(Amenities::getId, Function.identity()));
        List<FacilityDTO> facilityDTOList = buildAmenities(hotel, map);

        HotelDetail hotelDetail = HotelDetail.builder()
                .id(hotel.getId())
                .title(hotel.getTitle())
                .slug(hotel.getSlug())
                .code(hotel.getCode())
                .address(hotel.getAddress())
                .price(hotel.getMinPrice())
                .description(hotel.getDescription())
                .price(hotel.getMinPrice())
                .isFavorite(this.checkFavoriteHotel(hotel.getSlug()))
                .facilityDTOList(facilityDTOList)
                .build();
        buildImages(hotelDetail);

        //get all room of hotel
        List<HotelBookableItem> bookableItems = hotelBookItemRepository.findAllByHotelCodeOrderByPriceAsc(hotel.getCode());
        List<HotelRoomDTO> roomDTOList = MappingUtils.map(bookableItems, HotelRoomDTO.class);

        roomDTOList.forEach(room -> room.setFacilityDTOList(room.getAmenities()
                .stream().map(amenitiesId -> FacilityDTO.builder()
                        .id(amenitiesId.toString())
                        .name(map.get(amenitiesId).getName())
                        .icon(map.get(amenitiesId).getIcon())
                        .build())
                .collect(Collectors.toList())));

        return BaseResponse.ok(hotelDetail, roomDTOList);
    }


    private void buildImages(HotelDetail hotelDetail) {
        List<HotelImage> hotelImages = hotelImageRepository.findAllByHotelCode(hotelDetail.getCode());
        hotelDetail.setImages(MappingUtils.map(hotelImages, ImageDTO.class));
    }

    private List<FacilityDTO> buildAmenities(Hotel hotel, Map<Integer, Amenities> map) {
        List<Integer> amenitiesInt = Arrays.asList(hotel.getAmenities());

        return amenitiesInt.stream().map(amenitiesId -> FacilityDTO.builder()
                .id(amenitiesId.toString())
                .name(map.get(amenitiesId).getName())
                .icon(map.get(amenitiesId).getIcon())
                .build()).collect(Collectors.toList());
    }

    private boolean checkFavoriteHotel(String slug) {
        String userId = AuthenticationUtils.getUserId();
        if (userId != null) {
            return favoriteRepository.findByUserIdAndSlug(userId, slug).isPresent();
        }
        return false;
    }

    @Transactional
    public BaseResponse uploadHotel(HotelUploadBody hotelUploadBody) {
        String userId = AuthenticationUtils.getUserId();
        String hotelCode = HelperUtils.randomHotelCode();
        List<String> images = FileStore.getFilePaths(hotelUploadBody.getImageList(), "hotel");

        //luu image cua hotel vao table image
        List<HotelImage> hotelImages = new ArrayList<>();
        images.forEach(imageName -> hotelImages.add(HotelImage.builder()
                .hotelCode(hotelCode)
                .caption("image for hotel")
                .url(imageName)
                .imageType(ImageType.MAIN)
                .build()));
        hotelImageRepository.saveAll(hotelImages);

        //luu hotel vao table hotel
        Hotel hotel = Hotel.builder()
                .hotelType(HotelType.valueOf(hotelUploadBody.getType()))
                .code(hotelCode)
                .address(hotelUploadBody.getAddress())
                .amenities(hotelUploadBody.getAmenities())
                .slug(HelperUtils.toSlug(hotelUploadBody.getTitle() + hotelCode))
                .description(hotelUploadBody.getDescription())
                .rank(hotelUploadBody.getRank())
                .star(hotelUploadBody.getStar().doubleValue())
                .title(hotelUploadBody.getTitle())
                .userId(userId)
                .approveStatus(ApproveStatus.PENDING)
                .build();
        hotel = hotelRepository.save(hotel);
        hotelRepository.updateSearchVector(hotel.getCode());
        return BaseResponse.ok(hotel);
    }

    @Transactional
    public BaseResponse updateHotel(HotelUploadBody hotelUploadBody) {

        Hotel hotel = hotelRepository.findFirstByCode(hotelUploadBody.getHotelCode());
        if (hotel == null) throw new ResourceNotFoundException();

        List<String> images = FileStore.getFilePaths(hotelUploadBody.getImageList(), "hotel");

        //luu image cua hotel vao table image
        List<HotelImage> hotelImages = new ArrayList<>();
        images.forEach(imageName -> hotelImages.add(HotelImage.builder()
                .hotelCode(hotelUploadBody.getHotelCode())
                .caption("image for hotel")
                .url(imageName)
                .imageType(ImageType.MAIN)
                .build()));
        hotelImageRepository.saveAll(hotelImages);

        //luu hotel vao table hotel
        if (!hotelUploadBody.getAddress().isEmpty()) hotel.setAddress(hotelUploadBody.getAddress());
        if (hotelUploadBody.getAmenities() != null) hotel.setAmenities(hotelUploadBody.getAmenities());
        if (hotelUploadBody.getDescription() != null) hotel.setDescription(hotelUploadBody.getDescription());
        if (hotelUploadBody.getRank() != null) hotel.setRank(hotelUploadBody.getRank());
        if (hotelUploadBody.getStar() != null) hotel.setStar(hotelUploadBody.getStar().doubleValue());
        if (hotelUploadBody.getTitle() != null) hotel.setTitle(hotelUploadBody.getTitle());

        hotel = hotelRepository.save(hotel);
        hotelRepository.updateSearchVector(hotel.getCode());
        return BaseResponse.ok(hotel);
    }

    @Transactional
    public void uploadHotelRoom(HotelRoomUploadBody hotelRoomUploadBody) {
        String image = FileStore.getFilePath(hotelRoomUploadBody.getImage(), "hotel-room-");
        //luu image cua hotel vao table image
//        HotelImage hotelImage = HotelImage.builder()
//                .hotelCode(hotelRoomUploadBody.getHotelCode())
//                .caption("image for room hotel")
//                .url(image)
//                .imageType(ImageType.ROOM)
//                .build();
//        hotelImageRepository.save(hotelImage);

        Hotel hotel = hotelRepository.findFirstByCode(hotelRoomUploadBody.getHotelCode());
        if (hotel.getMinPrice() == null) {
            hotel.setMinPrice(hotelRoomUploadBody.getPrice());
        }
        if (hotelRoomUploadBody.getPrice() < hotel.getMinPrice()) {
            hotel.setMinPrice(hotelRoomUploadBody.getPrice());
        }
        hotelRepository.save(hotel);

        HotelBookableItem hotelBookableItem = HotelBookableItem.builder()
                .available(true)
                .roomType(RoomType.valueOf(hotelRoomUploadBody.getType()))
                .hotelCode(hotelRoomUploadBody.getHotelCode())
                .currency(hotelRoomUploadBody.getCurrency())
                .hotelOptionCode(hotelRoomUploadBody.getHotelOptionCode())
                .price(hotelRoomUploadBody.getPrice())
                .image(image)
                .amenities(hotelRoomUploadBody.getAmenities())
                .build();
        hotelBookItemRepository.save(hotelBookableItem);

    }

    @Transactional
    public void updateHotelRoom(HotelRoomUploadBody hotelRoomUploadBody) {
        HotelBookableItem room = hotelBookItemRepository.findFirstById(hotelRoomUploadBody.getRoomId());
        if (room == null) throw new ResourceNotFoundException();


        String image = FileStore.getFilePath(hotelRoomUploadBody.getImage(), "hotel-room-");
        //luu image cua hotel vao table image
//        HotelImage hotelImage = HotelImage.builder()
//                .hotelCode(hotelRoomUploadBody.getHotelCode())
//                .caption("image for room hotel")
//                .url(image)
//                .imageType(ImageType.ROOM)
//                .build();
//        hotelImageRepository.save(hotelImage);

        Hotel hotel = hotelRepository.findFirstByCode(hotelRoomUploadBody.getHotelCode());
        if (hotel.getMinPrice() == null) {
            hotel.setMinPrice(hotelRoomUploadBody.getPrice());
        }
        if (hotelRoomUploadBody.getPrice() < hotel.getMinPrice()) {
            hotel.setMinPrice(hotelRoomUploadBody.getPrice());
        }
        hotelRepository.save(hotel);

        if (hotelRoomUploadBody.getAmenities() != null) room.setAmenities(hotelRoomUploadBody.getAmenities());
        if (hotelRoomUploadBody.getPrice() != null) room.setPrice(hotelRoomUploadBody.getPrice());
        if (hotelRoomUploadBody.getType() != null) room.setRoomType(RoomType.valueOf(hotelRoomUploadBody.getType()));
        if (hotelRoomUploadBody.getHotelOptionCode() != null) room.setHotelOptionCode(hotelRoomUploadBody.getHotelOptionCode());
        if (image != null) room.setImage(image);
        hotelBookItemRepository.save(room);

    }

    public BaseResponse getListRoom(String hotelCode, Integer page, Integer perPage) {
        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        List<Amenities> amenitiesList = amenitiesRepository.findAll();
        Map<Integer, Amenities> map = amenitiesList.stream().collect(Collectors.toMap(Amenities::getId, Function.identity()));
        List<HotelBookableItem> roomList = hotelBookItemRepository.findAllByHotelCode(hotelCode, PageRequest.of(page, perPage));
        List<HotelRoomDTO> roomDTOList = MappingUtils.map(roomList, HotelRoomDTO.class);

        roomDTOList.forEach(room -> room.setFacilityDTOList(room.getAmenities()
                .stream().map(amenitiesId -> FacilityDTO.builder()
                        .id(amenitiesId.toString())
                        .name(map.get(amenitiesId).getName())
                        .icon(map.get(amenitiesId).getIcon())
                        .build())
                .collect(Collectors.toList())));
        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", hotelBookItemRepository.countAllByHotelCode(hotelCode));
        return BaseResponse.ok(roomDTOList, mapReturn);
    }

    public BaseResponse getListHotel(Integer page, Integer perPage) {
        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        List<Hotel> hotelList = hotelRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        List<String> productCodes = hotelList.stream().map(Hotel::getCode).collect(Collectors.toList());

        List<Amenities> amenitiesList = amenitiesRepository.findAll();
        Map<Integer, Amenities> map = amenitiesList.stream().collect(Collectors.toMap(Amenities::getId, Function.identity()));

        List<Hotel> hotelListQuery = hotelRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(page, perPage));
        List<HotelDTO> hotels = MappingUtils.map(hotelListQuery, HotelDTO.class);

        hotels.forEach(hotel -> hotel.setFacilityDTOList(hotel.getAmenities()
                .stream().map(amenitiesId -> FacilityDTO.builder()
                        .id(amenitiesId.toString())
                        .name(map.get(amenitiesId).getName())
                        .icon(map.get(amenitiesId).getIcon())
                        .build())
                .collect(Collectors.toList())));

        List<HotelImage> hotelImages = hotelImageRepository.findUniqueImage(productCodes);
        Map<String, String> mapImage = hotelImages.stream().collect(Collectors.toMap(HotelImage::getHotelCode, HotelImage::getUrl));

        hotels.forEach(hotel -> {
            String imageUrl = mapImage.get(hotel.getCode());
            if (imageUrl != null) {
                hotel.setImage(imageUrl);
            }
        });

        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", productCodes.size());
        return BaseResponse.ok(hotels, mapReturn);
    }

    @Transactional
    public void deleteHotel(String hotelCode) {
        hotelImageRepository.deleteAllByHotelCode(hotelCode);
        hotelBookItemRepository.deleteAllByHotelCode(hotelCode);
        hotelRepository.deleteAllByCode(hotelCode);
    }

    @Transactional
    public void deleteHotelRoom(Long roomId) {
        hotelBookItemRepository.deleteById(roomId);
    }

}
