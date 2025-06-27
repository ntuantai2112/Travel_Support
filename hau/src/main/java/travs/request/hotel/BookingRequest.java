/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travs.request.hotel;

import travs.model.Contact;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {

    private String hotelCode;

    private String packageId;

    private Contact contact;

    private Integer checkinDate;

    private Integer checkoutDate;

}
