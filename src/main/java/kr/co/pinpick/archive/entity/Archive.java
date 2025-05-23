package kr.co.pinpick.archive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.archive.dto.request.UpdateArchiveRequest;
import kr.co.pinpick.common.entity.BaseEntity;
import kr.co.pinpick.user.entity.FolderArchive;
import kr.co.pinpick.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "archives")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Archive extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(columnDefinition = "geometry")
    private Point location;

    @NotNull
    @Column(name = "position_x", nullable = false, precision = 10)
    private Double positionX;

    @NotNull
    @Column(name = "position_y", nullable = false, precision = 10)
    private Double positionY;

    @Size(max = 200)
    @Column(name = "address", length = 500)
    private String address;

    @Size(max = 200)
    @Column(name = "name", length = 200)
    private String name;

    @Size(max = 2000)
    @Column(name = "content", length = 2000)
    private String content;

    @NotNull
    @Column(name = "is_public", nullable = false)
    @Setter
    private Boolean isPublic;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    @Setter
    @Builder.Default
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repip_archive_id")
    private Archive repipArchive;

    @OneToMany(mappedBy = "archive", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("sequence desc")
    @Builder.Default
    private List<ArchiveAttach> archiveAttaches = new ArrayList<>();

    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    @Where(clause = "is_deleted = false")
    @Builder.Default
    private Set<ArchiveComment> archiveComments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    @Where(clause = "is_deleted = false")
    @Builder.Default
    private Set<ArchiveLike> archiveLikes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "archive", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("sequence desc")
    @Builder.Default
    private List<ArchiveTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    @Builder.Default
    private List<FolderArchive> folderArchives = new ArrayList<>();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateArchive(UpdateArchiveRequest request) {
        this.positionX = request.getPositionX();
        this.positionY = request.getPositionY();
        this.address = request.getAddress();
        this.name = request.getName();
        this.content = request.getContent();
        this.isPublic = request.getIsPublic();
    }
}
