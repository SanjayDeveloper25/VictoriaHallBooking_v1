package com.gcc.communityHall.dto;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
public class ErrorResponseDto {

    @Schema(
            description = "API path invoked by client"
    )
    private String apiPath;

    @Schema(
            description = "Error code representing the error occurred"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message representing the error occurred"
    )
    private String errorMessage;

    @Schema(
            description = "Timestamp when the error occurred"
    )
    private LocalDateTime errorTime;

	public String getApiPath() {
		return apiPath;
	}

	public void setApiPath(String apiPath) {
		this.apiPath = apiPath;
	}

	public HttpStatus getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(HttpStatus errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LocalDateTime getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(LocalDateTime errorTime) {
		this.errorTime = errorTime;
	}

	
	
}
