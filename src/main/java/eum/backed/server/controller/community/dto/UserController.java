package eum.backed.server.controller.community.dto;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
import eum.backed.server.lib.Helper;
import eum.backed.server.service.community.CustomUserService;
import eum.backed.server.service.community.UsersService;
import eum.backed.server.util.RequestUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
@Api(tags = "user")
public class UserController {
    FirebaseAuth firebaseAuth;
    @Autowired
    private CustomUserService customUserDetailsService;
    private final UsersService usersService;

    @PostMapping("/s")
    public ResponseEntity<?> register(@RequestHeader("Authorization") String authorization ,@RequestBody UsersRequestDTO.SignUp signUp) {
//        String idToken = request.get("idToken");
//        final FirebaseToken decodedToken;
        try {
            String token = RequestUtil.getAuthorizationToken(authorization);
            firebaseAuth.verifyIdToken(token);
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getEmail();
            customUserDetailsService.create(signUp, decodedToken);
            return ResponseEntity.ok(uid);
        } catch ( FirebaseAuthException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        // 근데
//        log.info(decodedToken.getEmail());

    }
    @GetMapping("sign")
    public ResponseEntity<?> signIn(@RequestHeader("Authorization") String authorization, Errors errors) throws FirebaseAuthException {
        String token = RequestUtil.getAuthorizationToken(authorization);
        firebaseAuth.verifyIdToken(token);
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        String uid = decodedToken.getEmail();
        return usersService.signIn(uid);

    }

    @GetMapping("/me")
    public String  getUserMe() {
//        Users customUser = (();
        return "ok";
    }
}
