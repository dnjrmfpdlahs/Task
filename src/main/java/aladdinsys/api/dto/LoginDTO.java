package aladdinsys.api.dto;

import lombok.Data;

public record LoginDTO(
    String userId,
    String password
) {

}