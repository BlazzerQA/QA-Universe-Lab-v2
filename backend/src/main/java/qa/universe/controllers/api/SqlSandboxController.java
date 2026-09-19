package qa.universe.controllers.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qa.universe.dto.SqlQueryRequest;
import qa.universe.dto.SqlQueryResponse;
import qa.universe.dto.SqlSchemaResponse;
import qa.universe.service.SqlSandboxService;

@RestController
@RequestMapping("/api/sql")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class SqlSandboxController {

    private final SqlSandboxService sqlSandboxService;

    @GetMapping("/schema")
    public SqlSchemaResponse schema() {
        return sqlSandboxService.schema();
    }

    @PostMapping("/query")
    public ResponseEntity<SqlQueryResponse> query(@RequestBody SqlQueryRequest request) {
        return ResponseEntity.ok(sqlSandboxService.execute(request.getSql()));
    }
}
