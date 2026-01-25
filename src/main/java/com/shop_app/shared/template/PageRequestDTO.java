package com.shop_app.shared.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    private int page;
    private int size;
    private String sortBy = "createdAt";
    private String direction = "desc";
}
