package kr.co.pinpick.user.entity;

import jakarta.persistence.*;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.BaseEntity;
import kr.co.pinpick.common.oauth.OAuth2Attributes;
import kr.co.pinpick.user.entity.enumerated.RoleType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "users")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
    private String email;

    private String nickname;

    private String password;

    private String profileImage;

    private String description;

    @OneToMany(mappedBy = "author")
    private List<Archive> archives;

    @OneToMany(mappedBy = "follow")
    private List<Follower> followers;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    @Setter
    @Builder.Default
    private RoleType role = RoleType.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + getRole());

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    public static User from(OAuth2Attributes attributes) {
        return builder()
                .email(attributes.getEmail())
                .build();
    }
}
