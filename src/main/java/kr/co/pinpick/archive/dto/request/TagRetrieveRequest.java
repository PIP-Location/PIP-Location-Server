package kr.co.pinpick.archive.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@NoArgsConstructor
@ParameterObject
@AllArgsConstructor
public class TagRetrieveRequest {
    private String q;

    private int pageNo;
}
