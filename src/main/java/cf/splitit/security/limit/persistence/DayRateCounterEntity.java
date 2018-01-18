package cf.splitit.security.limit.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dayRateCounters")
@Getter
@Setter
public class DayRateCounterEntity {

    @Id
    private String id;

    @Indexed
    private String key;

    private int dayOfYear;

    private int value;
}
