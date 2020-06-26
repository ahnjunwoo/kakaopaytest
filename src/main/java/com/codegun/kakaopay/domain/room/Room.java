package com.codegun.kakaopay.domain.room;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "room")
@NoArgsConstructor
public class Room {
    @Id
    @Column(name = "ROOM_ID",length = 40)
    private String roomId;

    @Column(length = 40)
    private String roomName;
}
