package com.codegun.kakaopay.domain.user;

import com.codegun.kakaopay.domain.room.Room;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long id;

    @Column(length = 40)
    private String userName;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "user_rooms",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Set<Room> rooms = new HashSet<>();
}
