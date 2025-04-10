package kr.co.pinpick.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.entity.BaseEntity;
import kr.co.pinpick.user.dto.request.UpdateUserRequest;
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
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Size(max = 50)
    @Column(name = "nickname", length = 50)
    private String nickname;

    private String password;

    private String profileImage;

    @Size(max = 50)
    @Column(name = "description", length = 200)
    private String description;

    @NotNull
    @Column(name = "is_agree_to_terms_of_service", nullable = false)
    @Builder.Default
    private Boolean isAgreeToTermsOfService = false;

    @NotNull
    @Column(name = "is_agree_to_privacy_policy", nullable = false)
    @Builder.Default
    private Boolean isAgreeToPrivacyPolicy = false;

    @OneToMany(mappedBy = "author")
    private List<Archive> archives;

    @OneToMany(mappedBy = "follow")
    private List<Follower> followers;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
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

    public void updateUserInfo(UpdateUserRequest request) {
        this.nickname = request.getNickname();
        this.description = request.getDescription();
        this.isAgreeToTermsOfService = request.getIsAgreeToTermsOfService();
        this.isAgreeToPrivacyPolicy = request.getIsAgreeToPrivacyPolicy();
    }
}
