package com.example.sync_book.model;

import com.example.sync_book.validation.NoHtml;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class NamedEntity extends BaseEntity{
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    private String name;
}
