import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
@AllArgsConstructor
@Data
public class Package  {
   private String location;
   private int distance;
   private int packageValue;
   private LocalDate deliveryDate;
}
