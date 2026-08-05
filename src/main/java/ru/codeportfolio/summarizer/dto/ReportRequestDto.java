package ru.codeportfolio.summarizer.dto;

import java.util.List;

public record ReportRequestDto(
        List<UserDto> usersDto
) {
}
