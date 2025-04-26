package kr.co.pinpick.user.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class BlockId implements Serializable {
    private Long user;

    private Long block;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockId blockId = (BlockId) o;
        return Objects.equals(user, blockId.user) && Objects.equals(block, blockId.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, block);
    }
}

