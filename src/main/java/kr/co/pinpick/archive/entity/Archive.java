package kr.co.pinpick.archive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.common.entity.BaseEntity;
import kr.co.pinpick.user.entity.FolderArchive;
import kr.co.pinpick.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
@NoArgsConstructor
@AllArgsConstructor
public class Archive extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repip_archive_id")
    private Archive repipArchive;

    @OneToMany(mappedBy = "archive", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("sequence desc")
    private List<ArchiveAttach> archiveAttaches = new ArrayList<>();

    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    private Set<ArchiveComment> archiveComments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    private Set<ArchiveLike> archiveLikes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "archive", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("sequence desc")
    private List<ArchiveTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "archive", cascade = CascadeType.ALL)
    private List<FolderArchive> folderArchives = new ArrayList<>();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
