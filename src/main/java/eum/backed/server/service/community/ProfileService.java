package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.ProfileRequestDTO;
import eum.backed.server.domain.community.mylevel.AvatarLevel;
import eum.backed.server.domain.community.mylevel.AvatarLevelRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.region.DONG.DongRepository;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.bank.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UsersRepository userRepository;
    private final DongRepository dongRepository;
    private final AvatarLevelRepository avatarLevelRepository;
    private final BankAccountService bankAccountService;
    public DataResponse create(ProfileRequestDTO.CreateProfile createProfile, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (profileRepository.existsByUser(getUser)) throw new IllegalArgumentException("이미 프로필이 있는 회원");
        Dong getDong = dongRepository.findByDong(createProfile.getDong()).orElseThrow(()-> new IllegalArgumentException("Invalid argument"));
        AvatarLevel getAvatarLevel = avatarLevelRepository.findByLevelName(createProfile.getAvatar()).orElseThrow(()->new IllegalArgumentException("초기 데이터 세팅 안되있어요"));
        validateNickname(createProfile.getNickname());

        Profile profile = Profile.t0Entity(createProfile,getDong, getAvatarLevel,getUser);
        profileRepository.save(profile);

        getUser.updateRole(Role.ROLE_USER);
        userRepository.save(getUser);
        bankAccountService.createUserBankAccount(createProfile.getNickname(), createProfile.getAccountPassword(),getUser);
        return new DataResponse<>().success("데이터 저장 성공");

    }
    private void validateNickname(String nickname){
        if(profileRepository.existsByNickname(nickname)) throw new IllegalArgumentException("이미 있는 닉네임");
    }

}
