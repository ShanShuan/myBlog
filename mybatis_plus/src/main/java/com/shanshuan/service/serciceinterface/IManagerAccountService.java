package com.shanshuan.service.serciceinterface;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanshuan.DO.ManageAccountDO;
import com.shanshuan.VO.CompanyAccountVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangzifeng on 2020/5/11.
 */
@Service
public interface IManagerAccountService {
    Page<CompanyAccountVo> getUserListByCompanyAndPageAndSearch(Page<CompanyAccountVo> page,
                                                                @Param("companyId") Integer companyId,
                                                                @Param("search") String search,
                                                                @Param("encryptSearch") String encryptSearch);

    List<ManageAccountDO> listByUserId(@Param("idList") List<Integer> idList);
}
