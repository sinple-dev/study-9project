package com.fastcampus.boardserver.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private int id;
    private String name;
    private String url;
    private int postId;
    private List<TagDTO> tagDTOList;
}