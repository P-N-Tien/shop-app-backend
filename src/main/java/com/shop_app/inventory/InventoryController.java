package com.shop_app.inventory;

import com.shop_app.inventory.request.CreateInventoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventories", description = "API for managing stocks")
@RestController
@RequestMapping("${api.prefix}/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final IInventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Create Stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Invalid")
    })
    public ResponseEntity<Void> createInventory(
            @Valid @RequestBody CreateInventoryRequest req
    ) {
        inventoryService.createInventory(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

