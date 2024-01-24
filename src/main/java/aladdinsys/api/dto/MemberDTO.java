package aladdinsys.api.dto;

import lombok.Data;

@Data
public class MemberDTO {
    public String userId;
    public String password;
    public String name;
    public String regNo;
}