package kr.co.pinpick.archive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "archive_attaches")
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveAttach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @Size(max = 200)
    @NotNull
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @NotNull
    @Column(name = "sequence", nullable = false, length = 200)
    private byte sequence;

    @Size(max = 200)
    @NotNull
    @Column(name = "path", nullable = false, length = 200)
    private String path;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;
}