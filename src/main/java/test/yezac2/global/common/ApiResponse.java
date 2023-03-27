package test.yezac2.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private T data;
	private Boolean result;
	private String resultMessage;

	public ApiResponse(T data) {
		this.data = data;
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<T>(data);
	}

	public static <T> ApiResponse<T> success() {
		return new ApiResponse<T>(null);
	}
}