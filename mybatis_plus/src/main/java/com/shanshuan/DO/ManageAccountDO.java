package com.shanshuan.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "manage_account")
public class ManageAccountDO {
    /**
     * 用户Id
     */
    @TableId(value = "id",type= IdType.AUTO)
    private Long id;


    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;


    /**
     * 用户别名
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 盐
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 用户类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 电子邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 是否有效(1-有效，2-无效，0-待配置)
     */
    @TableField(value = "is_valid")
    private Integer isValid;

    /**
     * 操作者
     */
    @TableField(value = "opt_id")
    private Integer optId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 0-未删除 1-删除
     */
    @TableField(value = "is_delete")
    private Boolean isDelete;

    /**
     * 所属企业(公司)
     */
    @TableField(value = "company_id")
    private Integer companyId;

    /**
     * 父级账号 母账号 0
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 用户名+姓名+手机号+邮箱+密码 md5加密
     */
    @TableField(value = "md5")
    private String md5;

    /**
     * 父级账户名称
     */
    @TableField(exist = false)
    private  String parentName;
}