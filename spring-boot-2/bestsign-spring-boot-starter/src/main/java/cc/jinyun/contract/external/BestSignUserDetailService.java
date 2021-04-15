package cc.jinyun.contract.external;


public interface BestSignUserDetailService {

    /**
     * 通过业务系统中的用户的唯一标识构建 框架需要的userDetail
     *
     * @param userId 用户id
     * @return userDetail
     */
    UserDetail getUserDetail(Long userId);

    /**
     * Return whether the given user has registered, so it can accelerate ...
     *
     * @param userId the given userId
     * @return true or false
     */
    boolean registered(Long userId);
}
