package qa.universe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlQueryResponse {

    private List<String> columns;
    private List<List<Object>> rows;
    private int rowCount;
}
