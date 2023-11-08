package eum.backed.server.domain.community.profile;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.controller.community.dto.request.ProfileRequestDTO;
import eum.backed.server.domain.community.avatar.Avatar;
import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Profile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column
    private String name;
    private String introduction;
    private String nickname;
    private String address;
    private int totalSunrisePay;


    @ManyToOne
    @JoinColumn(name = "dong_id")
    private Dong dong;

    @OneToOne
    @JoinColumn(name="my_leve_id")
    private Avatar avatar;

    @OneToOne
    @JoinColumn(name="ueer_id")
    private Users user;

    public static Profile t0Entity(ProfileRequestDTO.CreateProfile createProfile, Dong dong , Avatar avatar, Users user){
        return Profile.builder()
                .nickname(createProfile.getNickname())
                .dong(dong)
                .introduction(createProfile.getIntroduction())
                .totalSunrisePay(0)
                .avatar(avatar)
                .user(user)
                .build();
    }

}
