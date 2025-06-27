package travs.response.activities;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivitiesGameDTO implements Serializable {

    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "description", columnDefinition = "text")
    private String description;

}
