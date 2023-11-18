package eum.backed.server.service.community;

import eum.backed.server.domain.community.avatar.Avatar;
import eum.backed.server.domain.community.avatar.AvatarRepository;
import eum.backed.server.domain.community.avatar.Standard;
import eum.backed.server.domain.community.avatar.StandardRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LevelService {
    private final StandardRepository standardRepository;
    private final ProfileRepository profileRepository;
    private final AvatarRepository avatarRepository;
    public void levelUp(Profile profile){
        Standard checkLevel = checkLevel(profile.getTotalSunrisePay());
        if(profile.getAvatar().getStandard() != checkLevel ){
            Avatar avatar = avatarRepository.findByAvatarNameAndStandard(profile.getAvatar().getAvatarName(), checkLevel).orElseThrow(() -> new NullPointerException("매칭되는 아바타가 없습니다"));
            profile.upDateAvatar(avatar);
            profileRepository.save(profile);
            log.info(avatar.getAvatarLevelName().name());
        }
    }
    private Standard checkLevel(int totalSunrise){
        Standard cloud = standardRepository.findById(1L).orElseThrow(() -> new NullPointerException("초기 데이터 미설정"));
        Standard babySun = standardRepository.findById(2L).orElseThrow(() -> new NullPointerException("초기 데이터 미설정"));
        Standard sun = standardRepository.findById(3L).orElseThrow(() -> new NullPointerException("초기 데이터 미설정"));
        Standard organization = standardRepository.findById(4L).orElseThrow(() -> new NullPointerException("초기 데이터 미설정"));
        log.info(String.valueOf(totalSunrise));
        if(totalSunrise <=cloud.getStandard() && totalSunrise > 0){
            return cloud;
        } else if ( cloud.getStandard() < totalSunrise && totalSunrise<=babySun.getStandard()  ) {
            return babySun;
        } else if (babySun.getStandard()< totalSunrise ) {
            return sun;
        }return organization;
    }
}
