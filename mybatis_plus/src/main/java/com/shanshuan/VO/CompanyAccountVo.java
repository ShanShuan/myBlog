package com.shanshuan.VO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class CompanyAccountVo {

    /**
     * 账户id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 姓名
     */
    private String nickName;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 是否有效  1 - 有效 2 - 无效
     */
    private Integer isValid;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 母账号id
     */
    private Integer parentId;

    /**
     * 母账号
     */
    private String parentName;


    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;


    /**
     * 是否母账号
     */
    private boolean motherAccount;
}
