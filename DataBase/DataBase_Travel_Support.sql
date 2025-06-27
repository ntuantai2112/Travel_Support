======================================================== DATABASE TRAVEL_SUPPORT ================================================= 
*) TỔNG QUAN HỆ THỐNG CSDL TRAVEL_SUPPORT:
	-> Đây là một hệ thống hỗ trợ du lịch bao gồm nhiều bảng như:
		*) Tài khoản người dùng (account)
		*) Các hoạt động du lịch (activities, activities_game, activities_receipt,...)
		*) Khách sạn (+) hotel, +) hotel_image, +) hotel_receipt, +) hotel_bookable_item,...)
		*) Tiện ích (amenities, feature)
		*) Yêu thích (favorite, favorite_+) hotel)
		*) Bài đăng (post)
		*) Liên hệ đặt phòng (booking_contact)
		
1. Bảng account - Người dùng: Lưu thông tin người dùng trên hệ thống.
	*) Quan hệ: 1 Role -> N Account.

2. Bảng activities:  Quản lý các hoạt động du lịch (tour, event, trải nghiệm,...).
	*) Liên kết: 1 activity có thể có nhiều activities_image, activities_game, activities_receipt(Biên lai)
	*) Vd: Hoạt động: "Tour ngắm hoàng hôn Đà Nẵng", gồm hình ảnh, hóa đơn, và mini game tặng quà.
	!!!) Các thuộc tính trong bảng: 
		*) Tên cột adult_ticket_price, child_ticket_price: Giá vé người lớn/trẻ em
		*) approve_status: Trạng thái duyệt (PENDING, APPROVED, REJECTED)
		*) duration: Thời lượng (giờ)
		*) title, slug: Tên tour
		*) version: Mô tả: Số phiên bản của bản ghi (dùng cho optimistic locking) 
			*) Chức năng: Hỗ trợ kiểm soát song song (concurrent update), tránh mất dữ liệu khi nhiều người cùng sửa.
		*) type: Loại hoạt động du lịch (vd: 'Adventure', 'Cultural', 'Relaxation') - Chức năng: Phân loại tour/hoạt động (ví dụ: mạo hiểm, khám phá, nghỉ dưỡng,...)
		*) duration:  Thời lượng hoạt động (số giờ hoặc ngày) -  Chức năng: Cho biết người dùng phải dành bao nhiêu thời gian cho hoạt động này.
		*) slug: Chuỗi định danh URL thân thiện(vd:tour-leo-nui-da-nang) -  Chức năng: Tạo URL dễ nhớ, chuẩn SEO, ví dụ: /activities/tour-leo-nui-da-nang
		
		
3. Bảng activities_image: Lưu ảnh hoạt động. Cột chính:
	*) Quan hệ: 1 activity ➝ N activities_image
	!!!) Các thuộc tính trong bảng:
		*) activities_code: Mã tour (FK → activities.code) – Xác định tour nào sở hữu ảnh này.
		*) caption: Tiêu đề/mô tả ngắn cho ảnh. Dùng để chú thích ảnh trong giao diện (ví dụ: "Toàn cảnh khu du lịch").
		*) url: Đường dẫn ảnh – thường là link CDN hoặc server ảnh. Dùng để hiển thị trên giao diện người dùng.
	##) Mối quan hệ:
		*) Mỗi tour (activities) → có thể có N ảnh trong bảng activities_image
		*) 1 ảnh - thuộc 1 tour duy nhất.
		
		
4. Bảng activities_game: Mini game/hoạt động phụ trong chuyến đi
	*) Ví dụ: Trò chơi "Giải mã mật thư trên đảo" thuộc hoạt động chính "Tour khám phá Cù Lao Chàm".
	*) Quan hệ: 1 activity ➝ N activities_game

5. Bảng activities_receipt: Lưu thông tin thanh toán của khách cho 1 hoạt động - Hóa đơn đặt hoạt động
	*) Quan hệ : 
		+)activity_id: FK → activities(id)
		+)account_id: FK → account(id)
		+) N receipt → 1 booking_contact
		!!!) Các thuộc tính trong bảng: 
			*) activities_info: Dữ liệu tour
			*) number_ticket_adult, number_ticket_child: Sô vé đặt của người lớn và trẻ em.
			*) travel_date: Ngày đi
			*) user_id: Người đặt
			*) contact_id: FK → booking_contact
			
			
6. Bảng amenities: Tiện nghi phòng/khách sạn
	*) Ví dụ: Wi-Fi, hồ bơi, điều hòa
	*) Mục đích :
		+) Mô tả các tiện ích mà khách sạn hoặc nhà hàng cung cấp cho khách hàng.
		+) Dùng để hiển thị bằng biểu tượng trực quan trên frontend.
		+) Hỗ trợ lọc/so sánh giữa các địa điểm dựa trên tiện nghi.
	

7. Bảng feature: - Đặc điểm nổi bật của khách sạn, nhà hàng, 
	*) Ví dụ: Gần biển, có buffet sáng, view đẹp
	*) Mục đích :
		+) Giúp mô tả tính năng nổi bật của một khách sạn, nhà hàng hoặc tour.
		+) Hỗ trợ người dùng lọc kết quả theo nhu cầu (ví dụ: chỉ xem khách sạn "gần biển").
		+) Dùng để hiển thị trong UI theo dạng “đặc điểm nổi bật” hoặc “lý do nên chọn”.
	
	
	
8. Bảng hotel: Bảng chính quản lý khách sạn
	*) Liên kết với:
		+) hotel_image: Ảnh khách sạn
		+) hotel_bookable_item: Phòng có thể đặt   ===>>> Dùng để quản lý danh sách phòng, hình ảnh, đơn đặt phòng liên quan đến khách sạn.
		+) hotel_receipt: Hóa đơn đặt phòng
		+) favorite_hotel: Danh sách yêu thích
	!!!) Các thuộc tính trong bảng: 
		*) amenities: Danh sách tiện ích (FK → amenities)
			vd: Danh sách tiện nghi theo id của bảng amenities – lưu dưới dạng mảng số nguyên (VD: {1,3,4} tương ứng WiFi, Hồ bơi, Tivi)
		*) min_price: Giá thấp nhất
		*) star: Số sao
		*) title, slug: Tên khách sạn.
		*) approve_status: Trạng thái phê duyệt khách sạn trong hệ thống (APPROVED, PENDING, REJECTED)
		*) hotel_type: Loại khách sạn (Hotel, Resort, Homestay, v.v.) – giúp người dùng lọc theo loại
		*) rank: Điểm đánh giá tổng thể (1–10) – có thể được tính từ đánh giá người dùng
		*) slug: Chuỗi URL thân thiện, dùng để tạo đường dẫn: /khach-san/khach-san-nha-trang
	
	###) Mối quan hệ với các bảng: 
		*) 1 Hotel -> N hotel_bookable_item(phòng)
		*) 1 Hotel -> N hotel_image
		*) 1 Hotel -> N hotel_receipt ( Đơn đặt phòng)
		*) 1 Hotel -> N favorite_hotel (Danh sách yêu thích)

9. Bảng hotel_image: Ảnh của khách sạn
	*) Quan hệ: 1 hotel ➝ N hotel_image
	!!!) Các thuộc tính trong bảng:
		*) hotel_code: FK → hotel.code : Mã khách sạn (FK đến bảng hotel.code) – xác định ảnh thuộc về khách sạn nào
		*) url: Link ảnh
		*) Mô tả ảnh
		*) caption: Chú thích cho ảnh, hiển thị dưới ảnh trong frontend, ví dụ: "Ảnh phòng Deluxe"
		*) image_type: Loại ảnh: ví dụ "general", "room", "gallery", "amenity"... giúp nhóm ảnh đúng vị trí giao diện
	*) 1 khách sạn (hotel) ↔ N ảnh (hotel_image) 
	*) Liên kết qua cột hotel_code.


	
10. Bảng hotel_bookable_item - Phòng có thể đặt
	*) Loại phòng có thể đặt
	*) Ví dụ: Phòng đơn, phòng đôi, phòng VIP
	*) Quan hệ: 1 hotel ➝ N hotel_bookable_item
	!!!) Các thuộc tính trong bảng: 
		*) hotel_code: FK → hotel.code: Mã khách sạn (FK tới bảng hotel.code) – xác định phòng thuộc khách sạn nào
		*) room_type: Loại phòng
		*) price: Giá phòng
		*) available: Còn trống hay không
		*) amenities: anh sách tiện nghi của phòng (liên kết với bảng amenities, ví dụ: {1,2,3} = WiFi, máy lạnh, hồ bơi)
		*) currency: Đơn vị tiền tệ, ví dụ: VND, USD
		*) hotel_option_code: Mã riêng cho loại phòng, ví dụ: HTL001-DELUXE
		*) room_type: Loại phòng: Deluxe, Standard, Suite, Family,...
		
		
	
		
11. Bảng hotel_receipt: Hóa đơn đặt phòng
	*) Quan hệ:
		+) 1 account ➝ N hotel_receipt
		+) 1 hotel_bookable_item ➝ N hotel_receipt
	!!!) Các thuộc tính trong bảng:
		*) booking_id: Mã đặt phòng, có thể hiển thị cho người dùng (vd: BKH001)
		*) hotel_info: Dữ liệu khách sạn, Thông tin khách sạn và phòng, lưu dưới dạng JSON (vd: { "hotel": "HTL001", "room": "Deluxe" })
		*) user_id: Người đặt
		*) contact_id: FK → booking_contact
		*) from_date: Ngày bắt đầu đặt (vd: 20240623 ⇒ 23/06/2024)
		*) to_date: Ngày trả phòng (vd: 20240625)
		*) package_info: Thông tin gói dịch vụ đi kèm (vd: { "package": "Gói nghỉ dưỡng" })
		*) partner_id: 	ID đối tác (partner) quản lý khách sạn (FK đến bảng account.id)
		*) phone_partner: Số điện thoại liên hệ của đối tác
		*) total_nights: Tổng số đêm đặt (to_date - from_date)
		*) status: Trạng thái đơn: BOOKED, CANCELLED, COMPLETED
		*) contact_id: Người liên hệ khi nhận phòng (FK đến booking_contact.id)
		

12. Bảng favorite & favorite_hotel: Danh sách yêu thích của người dùng
	*) Quan hệ: 
		+) 1 account ➝ N favorite_hotel
		+) 1 hotel ➝ N favorite_hotel
	!!!) Các thuộc tính trong bảng: 
		*) slug, title, type: Loại mục yêu thích
		*) image_url: Ảnh đại diện của mục
		*) user_id: Người yêu thích
		*) type: Loại (hotel, activity, ...)
		*) slug: Đường dẫn thân thiện (SEO-friendly), dùng trong URL: /yeu-thich/slug-fav1
		*) type: Loại đối tượng được yêu thích: hotel, activity, restaurant, post, v.v.
	
	##) Mối quan hệ:
		*) 1 user_id (người dùng) → có nhiều favorite
		*) type sẽ xác định mục yêu thích này tham chiếu đến bảng nào: hotel, activity , restaurant , post 
		
	###) Bảng favorite_hotel:
		*)hotel_data: Dữ liệu khách sạn (nhúng) - Toàn bộ thông tin khách sạn
		*) user_id: Người yêu thích
		*) id, slug, product_code: Thông tin khách sạn
		*) hotel_data: Thông tin tóm tắt khách sạn được lưu tại thời điểm người dùng yêu thích (ví dụ: { "code": "HTL001", "name": "Hotel 1" })
		*) product_code: Mã định danh khách sạn chính thức (FK → hotel.code)
		*) slug: Chuỗi URL thân thiện, dùng để xây dựng link: /yeu-thich-khach-san/hotel-fav-1
		
		$$$) Mối quan hệ:
			+) user_id → account.id -> N-1 : Nhiều bản ghi yêu thích thuộc về 1 người dùng
				*) Một người dùng (account) có thể lưu nhiều khách sạn vào danh sách yêu thích.
				*) Nhưng mỗi bản ghi yêu thích (favorite_hotel) chỉ thuộc về 1 người dùng duy nhất.
			+) product_code → hotel.code -> N-1 : Nhiều lượt yêu thích thuộc về 1 khách sạn
				*) Một khách sạn (hotel.code) có thể được nhiều người dùng yêu thích.
				*) Nhưng mỗi bản ghi favorite_hotel chỉ tham chiếu đến 1 khách sạn cụ thể.
			

13. Bảng booking_contact: Liên hệ đặt phòng hoặc hỏi thông tin - Người liên hệ
	*) Ví dụ: Người dùng A gửi form "Tôi muốn đặt phòng ngày 20/6"
	!!!) Các thuộc tính trong bảng: 
		*) customer_id: Người đại diện (nếu có)
	
		

14. Bảng Post: Bài viết blog/kinh nghiệm du lịch
	*) Các bài viết sẽ được hiển thị trong giao diện web
	*) 
	
15. Bảng restaurant - Nhà hàng
	!!!) Các thuộc tính trong bảng: 
		*) restaurant_type: Loại hình (buffet, lẩu, nướng...)
		*) approve_status: Trạng thái duyệt (APPROVED, PENDING, REJECTED) – dùng để kiểm duyệt nội dung hiển thị
		*) code: Mã nhà hàng kỹ thuật – định danh duy nhất dùng để liên kết (vd: RS001)
		*) feature: Mảng ID đặc điểm nổi bật – FK đến bảng feature, VD: {1,2,5} tương ứng "Gần biển", "Buffet sáng", "Gần trung tâm"
		*) slug: Chuỗi URL thân thiện – dùng trong link: /nha-hang/nha-hang-hai-san
		
	##) Môi quan hệ 
		*) user_id -> account(id): N:1 -> Nhiều nhà hàng thuộc về 1 người dùng (partner/admin)
		*) feature -> feature(id): N:1 (mảng) -> Một nhà hàng có nhiều đặc điểm, mỗi đặc điểm dùng chung với nhiều nhà hàng
		*) code-> restaurant_image, restaurant_menu, restaurant_receipt: 1-N -> Một nhà hàng có nhiều ảnh, menu, hóa đơn
		
		
		
		
16. Bảng restaurant_image – Hình ảnh nhà hàng
	!!!) Các thuộc tính trong bảng: 
		*) restaurant_code: FK
		*) url, caption, image_type: Ảnh & mô tả

17. Bảng restaurant_menu - Thực đơn nhà hàng
	!!!) Các thuộc tính trong bảng: 
		*) restaurant_code: FK
		*) name, price, available, description, image: Món ăn
		*) available: Trạng thái còn bán hay không (true = còn bán)
		*) restaurant_code: Mã nhà hàng món ăn thuộc về (FK → restaurant.code)

18. Bảng restaurant_receipt: Hóa đơn nhà hàng
	!!!) Các thuộc tính trong bảng: 
		*) restaurant_info: Dữ liệu nhà hàng
		*) checkin_day/time: Ngày giờ đến nhà hàng
		*) number_adult/child, price: Số lượng trong đơn hàng & giá
		*) user_id, contact_id: Người đặt, người liên hệ
		*) booking_id: Mã đơn đặt hàng – mã hóa đơn do hệ thống tạo (VD: RSBOOK001)
		*) number_adult: Số người lớn trong đơn đặt
		*) number_child: Số trẻ em trong đơn đặt
		*) partner_id: ID đối tác (user ID) – người quản lý nhà hàng đó
		*) phone_partner: 	Số điện thoại đối tác – có thể hiển thị cho liên hệ xác nhận
		*) restaurant_info: Dữ liệu JSON chứa thông tin nhà hàng: tên, bàn, vị trí,...
		*) status: Trạng thái hóa đơn: BOOKED(Đã đặt), CANCELLED(Hủy bỏ), COMPLETED(Hoàn thành),..
		
	###) Mối quan hệ:
		*) user_id → account.id (N:1): Một user (tài khoản người dùng) có thể đặt nhiều đơn đặt bàn.
			+) Nhưng mỗi hóa đơn chỉ thuộc về 1 user duy nhất.- Nhưng mỗi hóa đơn chỉ thuộc về 1 user duy nhất.
		*) contact_id → booking_contact.id (N:1)
			+) Một người liên hệ (booking_contact) có thể được dùng cho nhiều hóa đơn.
			+) Nhưng mỗi hóa đơn chỉ có 1 người nhận bàn.
		*) partner_id → account.id (N:1):
			+) Một partner (đối tác) quản lý nhà hàng có thể có nhiều hóa đơn do khách đặt.
			+) Mỗi hóa đơn chỉ gắn với 1 partner cụ thể.
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
=========================================================================================================================================		
####) Mối quan hệ giữa các bảng trong CSDL:
	*) account.role_id → role.id: N-1 -> Người dùng thuộc vai trò, 1 vai trò có nhiều người dùng - nhiều người dùng thuộc 1 vai trò
	*) hotel.code → hotel_bookable_item.hotel_code: 10-N -> 1 Khách sạn có nhiều phòng
	*) hotel.code → hotel_image.hotel_code: 1-N -> 1 Khách sạn có nhiều hình ảnh
	*) restaurant.code → restaurant_menu.restaurant_code: 1-N -> 1 Nhà hàng có nhiều món
	*) restaurant.code → restaurant_image.restaurant_code: 1-N -> 1 Nhà hàng có nhiều hình ảnh
	*) *_receipt.contact_id → booking_contact.id: N-1 -> 1 người liên hệ có nhiều đơn hàng - hóa đơn(khách sạn, tour,nhà hàng,...) , mỗi hóa đơn - đơn hàng thì chỉ liên kết tới một người liên hệ duy nhất 
		
		
		
		
		
		
	
**) Chức năng trong website
	*) Đăng ký/đăng nhập : bảng account
	*) Tìm & xem khách sạn: bảng hotel, hotel_image, hotel_bookable_item, feature, amenities
	*) Đặt phòng: bảng hotel_receipt
	*) Xem & đặt tour/hoạt động: Bảng activities, activities_image, activities_receipt
	*) Mini game trong tour: Bảng activities_game
	*) Yêu thích khách sạn/hoạt động: Bảng favorite_hotel, favorite
	*) Bài viết: Bảng post
	*) Liên hệ hỗ trợ: Bảng	booking_contact


