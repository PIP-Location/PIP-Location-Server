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
public class ArchiveLIkeId implements Serializable {
    private Long author;

    private Long archive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArchiveLIkeId archiveLikeId = (ArchiveLIkeId) o;
        return Objects.equals(author, archiveLikeId.author) &&
                Objects.equals(archive, archiveLikeId.archive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, archive);
    }
}
