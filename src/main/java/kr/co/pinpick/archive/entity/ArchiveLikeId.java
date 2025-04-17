package kr.co.pinpick.archive.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class ArchiveLikeId implements Serializable {
    private Long user;

    private Long archive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArchiveLikeId archiveLikeId = (ArchiveLikeId) o;
        return Objects.equals(user, archiveLikeId.user) &&
                Objects.equals(archive, archiveLikeId.archive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, archive);
    }
}
