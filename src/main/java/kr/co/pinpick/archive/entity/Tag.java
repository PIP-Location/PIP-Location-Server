package kr.co.pinpick.archive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseEntity {
    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "count")
    private Integer count;
}