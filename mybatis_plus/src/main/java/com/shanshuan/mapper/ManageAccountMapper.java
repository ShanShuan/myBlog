package com.shanshuan.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanshuan.DO.ManageAccountDO;
import com.shanshuan.VO.CompanyAccountVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface ManageAccountMapper extends BaseMapper<ManageAccountDO> {


    /**
     * 查询公司人员列表
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param wrapper
     * @return
     */
    @Select(" SELECT  ma.id,ma.user_name  userName,ma.nickname  nickName,ma.phone  telephone,ma.is_valid  isValid,ma.email,ma.parent_id   parentId  FROM  manage_account ma ${ew.customSqlSegment}")
    Page<CompanyAccountVo> getUserListByCompanyAndPageAndSearch(Page<CompanyAccountVo> page,@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);


}