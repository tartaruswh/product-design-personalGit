package com.bewg.pd.baseinfo.modules.mapper;

import java.util.List;

import com.bewg.pd.baseinfo.modules.entity.dto.AttachedFileDTO;
import org.apache.ibatis.annotations.Param;
import com.bewg.pd.baseinfo.modules.entity.AttachedFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 模板附属文件表
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
@Mapper
public interface AttachedFileMapper extends BaseMapper<AttachedFile> {

    @Select({"<script>", "SELECT id, file_name, type, file_extension, path FROM t_attached_file ", "<where> ", "<trim prefixOverrides='AND'>", "<if test='type != null and type != \"\"'> AND type=#{type} </if>",
        "<if test='templateWorkbookId != null and templateWorkbookId != \"\" or templateWorkbookId == 0'> AND template_workbook_id=#{templateWorkbookId} </if>",
        "<if test='productMemberId != null and productMemberId != \"\"'> AND product_member_id=#{productMemberId} </if>",
        "<if test='releaseStatus != null and releaseStatus != \"\" or releaseStatus == 0'> AND release_status=#{releaseStatus} </if>",
        "<if test='createBy != null and createBy != \"\"'> AND create_by=#{createBy} </if>", "AND is_del=0", "</trim>", "</where>", "</script>"})
    List<AttachedFile> selectAttachedFileList(AttachedFileDTO attachedFileDTO);
}
