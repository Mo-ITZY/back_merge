package Collabo.MoITZY.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String token; // 로그인 토큰
    private int exprTime; // 토큰 만료 시간
    private String role; // 사용자 권한
}