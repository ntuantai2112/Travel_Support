/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travs.request.restaurant;

import travs.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRestaurantRequest {

    private String restaurantCode;

    private Contact contact;

    private Integer checkinDate;

    private String checkinTime;

    private Integer numberChild;

    private Integer numberAdult;

}
