package travs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String id;
    private String name;
    private String password;
    private String phone;
    private String email;
    private Boolean enabled;
    private String role;
    private String image;
    private String dob;
    private Long roleId;

    @JsonIgnore
    private MultipartFile multipartFile;

}
