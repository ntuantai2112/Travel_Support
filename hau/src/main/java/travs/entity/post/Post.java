package travs.entity.post;

import travs.entity.BaseTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post  extends BaseTimestamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    //columnDefinition giúp cấu hình kiểu dữ liệu ở trong mysql
    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "title")
    private String  title ;

    @Column(name = "image")
    private String image;

}
