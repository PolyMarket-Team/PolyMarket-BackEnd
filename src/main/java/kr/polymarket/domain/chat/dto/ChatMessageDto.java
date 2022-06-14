package kr.polymarket.domain.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@ApiModel(value = "채팅 메시지")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessageDto {

    @ApiModelProperty(name = "채팅방 Id", notes = "채팅방 Id")
    private String chatRoomId;

    @ApiModelProperty(name = "채팅을 보낸 유저 Id", notes = "채팅을 보낸 유저 Id")
    private Long senderId;

    @ApiModelProperty(name = "채팅내용", notes = "채팅내용")
    private String content;

    @ApiModelProperty(name = "채팅 수신유저 Id", notes = "채팅 수신유저 Id")
    private Long receiverId;

    @ApiModelProperty(name = "채팅 보낸 날짜", notes = "채팅 보낸 날짜")
    private String createDate;

}
