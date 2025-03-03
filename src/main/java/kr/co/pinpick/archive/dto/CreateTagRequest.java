package kr.co.pinpick.archive.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.common.rule.UniqueByAble;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class CreateTagRequest implements UniqueByAble {
    @Size(min = 1, max = 50)
    @NotNull
    private String name;

    @Override
    public Object getUniqueKey() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("[name: %s]", name);
    }
}