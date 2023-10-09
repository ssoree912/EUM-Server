package eum.backed.server.controller.community.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Date;

public class UsersRequestDTO {
    @Getter
    @Setter
    public static class SignUp{
        @NotEmpty(message = "이메일을 입력하세요")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;
        private String introduction;
        private String name;
        private String nickname;
        private Date birth;
        private String address;
        private String phone;
        private String sex;
        private boolean isBanned = false;
        private int totalVolunteerTime = 0;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
//        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

    }
    @Getter
    @Setter
    public static class SignIn{
        @NotEmpty(message = "이메일을 입력하세요")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication(){
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }
    @Getter
    @Setter
    public static class AuthSignin{
        private String idtoken;
    }
    @Getter
    @Setter
    public static class Reissue {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }
    @Getter
    @Setter
    public static class Logout {

        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        private String refreshToken;

    }
    @Getter
    @Setter
    public static class Test{
        private String idtoken;
        private String introduction;
        private String name;
        private String nickname;
        private Date birth;
        private String address;
        private String phone;
        private String sex;
        private boolean isBanned = false;
        private int totalVolunteerTime = 0;


    }
}
