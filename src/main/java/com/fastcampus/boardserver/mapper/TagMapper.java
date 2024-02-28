package com.fastcampus.boardserver.mapper;

import com.fastcampus.boardserver.dto.TagDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TagMapper {
    public int register(TagDTO tagDTO);

    public void updateTags(TagDTO tagDTO);

    public void deletePostTag(int tagId);

    public void createPostTag(@Param("tagId") Integer tagId, @Param("postId") Integer postId);

}
