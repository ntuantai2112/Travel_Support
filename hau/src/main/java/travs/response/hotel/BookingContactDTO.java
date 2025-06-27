package travs.response.hotel;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingContactDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String customerId;

}
