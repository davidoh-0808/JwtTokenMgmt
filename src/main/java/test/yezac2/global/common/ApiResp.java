package test.yezac2.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResp<T> {

	private T data;
	private Boolean result;
	private String resultMessage;

	public ApiResp(T data) {
		this.data = data;
	}

	public static <T> ApiResp<T> success(T data) {
		return new ApiResp<T>(data);
	}

	public static <T> ApiResp<T> success() {
		return new ApiResp<T>(null);
	}
}