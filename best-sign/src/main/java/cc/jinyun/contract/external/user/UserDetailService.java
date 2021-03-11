package cc.jinyun.contract.external.user;


public interface UserDetailService {

    /**
     * 通过业务系统中的用户的唯一标识构建 框架需要的userDetail
     *
     * @param userId 用户id
     * @return userDetail
     */
    UserDetail getUserDetail(Long userId);
}
